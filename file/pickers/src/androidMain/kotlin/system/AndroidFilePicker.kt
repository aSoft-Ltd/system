package system

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import koncurrent.Later
import koncurrent.later.andThen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import system.file.FilePicker
import system.file.mime.All
import system.file.mime.Mime
import system.file.toResponse
import system.internal.LocalFileImpl

class AndroidFilePicker(private val activity: ComponentActivity) : FilePicker {
    private var scope: CoroutineScope? = null
    private val permission by lazy { AndroidFilePickerPermissionManager(activity, scope) }
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

    private fun launchPicker(
        mimes: List<Mime>,
        multiple: Boolean
    ) = Later { resolve, reject ->
        val s = scope ?: return@Later reject(
            IllegalStateException("AndroidFileChooser has not been registered")
        )
        val l = launcher ?: return@Later reject(
            IllegalStateException("AndroidFileChooser has not been registered")
        )

        l.launch(mimes.toReadPermissions().toTypedArray())

        s.launch {
            val files = results.receive().mapNotNull { it.path }.map { LocalFileImpl(it) }
            resolve(files.toResponse(multiple))
        }
    }

    override fun openPicker(
        mimes: List<Mime>,
        multiple: Boolean
    ): Later<PickerResponse> {
        if (mimes.isEmpty()) return openPicker(listOf(All), multiple)
        if (permission.check(mimes) == Permission.Granted) return launchPicker(mimes, multiple)
        return permission.request(mimes).andThen { permit ->
            when (permit) {
                Permission.Granted -> launchPicker(mimes, multiple)
                else -> Later(PickerResponse.Denied)
            }
        }
    }

    fun unregister() {
        permission.unregister()
        scope?.cancel()
        scope = null
    }
}