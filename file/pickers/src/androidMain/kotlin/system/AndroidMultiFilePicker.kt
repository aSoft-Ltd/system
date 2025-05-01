package system

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import system.file.MultiFilePicker
import system.file.PickerLimit
import system.file.mime.Image
import system.file.mime.Mime
import system.file.mime.Video
import system.file.picker.response.Denied
import system.file.picker.response.MultiPickerResponse
import system.file.toResponse
import system.internal.FileInfo
import system.internal.LocalFile

class AndroidMultiFilePicker(private val activity: ComponentActivity) : MultiFilePicker {
    private var scope: CoroutineScope? = null
    private val permission by lazy { AndroidPickerPermissionManager(activity, scope) }
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

    private suspend fun show(
        mimes: List<Mime>,
        limit: PickerLimit,
    ): MultiPickerResponse {
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        l.launch(mimes.map { it.text }.toTypedArray())
        val files = results.receive().map { LocalFile(it) }
        val info = files.map { FileInfo(activity, it) }
        return files.toResponse(mimes, limit, info)
    }

    override suspend fun open(
        mimes: List<Mime>,
        limit: PickerLimit,
    ): MultiPickerResponse {
        if (mimes.isEmpty()) return open(listOf(Image, Video), limit)
        return try {
            if (permission.check(mimes) == Permission.Granted) return show(mimes, limit)
            if (permission.request(mimes) == Permission.Granted) return show(mimes, limit)
            Denied
        } finally {
            Denied
        }
    }

    fun unregister() {
        permission.unregister()
        scope?.cancel()
        scope = null
    }
}