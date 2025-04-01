package system

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import koncurrent.Later
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import system.internal.AndroidFileChooserPermissionManager
import system.internal.LocalFileImpl
import system.picker.files.FilePicker

class AndroidFilePicker(private val activity: ComponentActivity) : FilePicker {

    private var scope: CoroutineScope? = null
    override val permission by lazy { AndroidFileChooserPermissionManager(activity, scope) }
    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private val results by lazy { Channel<List<Uri>>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            scope?.launch { results.send(uris) }
        }
        permission.register()
    }

    override fun openFileChooser(
        extensions: List<String>,
        multiple: Boolean
    ) = Later<List<LocalFile>> { resolve, reject ->
        val s = scope ?: return@Later reject(
            IllegalStateException("AndroidFileChooser has not been registered")
        )
        val l = launcher ?: return@Later reject(
            IllegalStateException("AndroidFileChooser has not been registered")
        )

        l.launch(permission.permissions.toTypedArray())

        s.launch {
            val result = results.receive().mapNotNull { it.path }
            resolve(result.map { LocalFileImpl(it) })
        }
    }

    override fun openDirChooser() = TODO("Not yet implemented")

    fun unregister() {
        permission.unregister()
        scope?.cancel()
        scope = null
    }
}