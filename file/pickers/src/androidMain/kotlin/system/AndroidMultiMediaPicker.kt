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
import system.file.MultiMediaPicker
import system.file.PickerLimit
import system.file.PickingException
import system.file.mime.Image
import system.file.mime.MediaMime
import system.file.mime.Mime
import system.file.mime.Video
import system.file.picker.response.Denied
import system.file.picker.response.MultiPickerResponse
import system.file.toResponse
import system.internal.FileInfo
import system.internal.LocalFile

class AndroidMultiMediaPicker(private val activity: ComponentActivity) : MultiMediaPicker {
    private var scope: CoroutineScope? = null
    private val permission by lazy { AndroidPickerPermissionManager(activity, scope) }
    private var launcher: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private val results by lazy { Channel<List<Uri>>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            scope?.launch { results.send(uris) }
        }
        permission.register()
    }

    private suspend fun show(
        mimes: List<Mime>,
        limit: PickerLimit,
    ): MultiPickerResponse {
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(mimes.toMediaType())
            .setMaxItems(limit.count)
            .build()
        l.launch(request)
        val files = results.receive().map { LocalFile(it) }
        val errors = buildList {
            if (files.size > limit.count) {
                add(PickingException.CountLimitExceeded(files.size, limit.count))
            }
            for (file in files.map { FileInfo(activity, it) }) {
                val mime = Mime.from(file.extension())
                val name = file.name()
                if (mimes.none { it.matches(mime) }) add(PickingException.InvalidMimeType(name, mime, mimes))
                val size = file.size()
                if (size > limit.size) add(PickingException.SizeLimitExceeded(name, size, limit.size))
            }
        }
        return files.toResponse(errors)
    }

    override suspend fun open(
        mimes: List<MediaMime>,
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