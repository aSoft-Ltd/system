package system

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import system.file.PickerPermissionsManager
import system.file.mime.Mime

class AndroidPickerPermissionManager(
    private val activity: ComponentActivity,
    private var scope: CoroutineScope?
) : PickerPermissionsManager {
    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private val results by lazy { Channel<Map<String, Boolean>>() }

    fun register() {
        if (launcher != null) return
        launcher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            scope?.launch { results.send(it) }
        }
    }

    override fun check(mimes: List<Mime>): Permission {
        val permissions = mimes.toReadPermissions()
        if (permissions.isEmpty()) { // Old version of android that requires no permission at all
            return Permission.Granted
        }
        val granted = permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
        return if (granted) Permission.Granted else Permission.Unauthorized
    }

    override suspend fun request(mimes: List<Mime>): Permission {
        if (mimes.isEmpty()) return Permission.Unauthorized

        if (check(mimes) == Permission.Granted) return Permission.Granted

        val l = launcher ?: throw IllegalStateException("Permission manager not registered")

        val permissions = mimes.toReadPermissions()
        l.launch(permissions.toTypedArray())
        val results = results.receive()
        val granted = results.all { it.value }
        return if (granted) Permission.Granted else Permission.Denied
    }

    fun unregister() {
        launcher?.unregister()
        launcher = null
    }
}