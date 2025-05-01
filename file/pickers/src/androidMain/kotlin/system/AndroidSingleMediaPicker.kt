package system

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import system.file.PickerLimit
import system.file.SingleMediaPicker
import system.file.mime.Image
import system.file.mime.MediaMime
import system.file.mime.Mime
import system.file.mime.Video
import system.file.picker.response.Cancelled
import system.file.picker.response.Denied
import system.file.picker.response.SinglePickerResponse
import system.file.picker.response.toSingle
import system.file.toResponse
import system.internal.FileInfo
import system.internal.LocalFile

class AndroidSingleMediaPicker(private val activity: ComponentActivity) : SingleMediaPicker {
    private var scope: CoroutineScope? = null
    private val permission by lazy { AndroidPickerPermissionManager(activity, scope) }
    private var launcher: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private val results by lazy { Channel<Uri?>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            scope?.launch { results.send(uri) }
        }
        permission.register()
    }

    private suspend fun show(
        mimes: List<Mime>,
        limit: MemorySize,
    ): SinglePickerResponse {
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(mimes.toMediaType())
            .build()
        l.launch(request)
        val files = listOf(results.receive()?.let { LocalFile(it) } ?: return Cancelled)
        val infos = files.map { FileInfo(activity, it) }
        return files.toResponse(mimes, PickerLimit(limit, 1), infos).toSingle()
    }

    override suspend fun open(
        mimes: List<MediaMime>,
        limit: MemorySize,
    ): SinglePickerResponse {
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