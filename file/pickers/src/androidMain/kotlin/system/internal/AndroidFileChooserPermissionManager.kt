package system.internal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import koncurrent.Later
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import system.Permission
import system.picker.files.FilePickerPermissionsManager

class AndroidFileChooserPermissionManager(
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

    override fun check(): Permission {
        val granted = permissions.all {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
        }
        return if (granted) Permission.Granted else Permission.Denied
    }

    internal val permissions by lazy {
        buildSet {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_AUDIO)
                add(Manifest.permission.READ_MEDIA_VIDEO)
                add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }

    override fun request() = Later<Permission> { resolve, reject ->
        if (check() == Permission.Granted) {
            return@Later resolve(Permission.Granted)
        }
        val l = launcher ?: return@Later reject(
            IllegalStateException("Permission manager not registered")
        )
        val s = scope ?: return@Later reject(
            IllegalStateException("Permission manager not initialized")
        )
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