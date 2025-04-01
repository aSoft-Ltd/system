package system

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import koncurrent.Later
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import system.file.mime.Mime
import system.file.FilePickerPermissionsManager

class AndroidFilePickerPermissionManager(
    private val activity: ComponentActivity,
    private var scope: CoroutineScope?
) : FilePickerPermissionsManager {
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

    override fun request(mimes: List<Mime>) = Later<Permission> { resolve, reject ->
        if (mimes.isEmpty()) return@Later resolve(Permission.Granted)

        if (check(mimes) == Permission.Granted) {
            return@Later resolve(Permission.Granted)
        }
        val l = launcher ?: return@Later reject(
            IllegalStateException("Permission manager not registered")
        )
        val s = scope ?: return@Later reject(
            IllegalStateException("Permission manager not initialized")
        )
        val permissions = mimes.toReadPermissions()
        l.launch(permissions.toTypedArray())
        s.launch {
            val results = results.receive()
            val granted = results.all { it.value }
            resolve(if (granted) Permission.Granted else Permission.Denied)
        }
    }

    fun unregister() {
        launcher?.unregister()
        launcher = null
    }
}