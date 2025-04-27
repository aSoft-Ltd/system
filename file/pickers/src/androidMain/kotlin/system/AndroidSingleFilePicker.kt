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
import system.file.SingleFilePicker
import system.file.fits
import system.file.mime.Image
import system.file.mime.Mime
import system.file.mime.Video
import system.file.picker.response.Cancelled
import system.file.picker.response.Denied
import system.file.picker.response.Failure
import system.file.picker.response.FilePicked
import system.file.picker.response.SinglePickerResponse
import system.internal.FileInfo
import system.internal.LocalFile

class AndroidSingleFilePicker(private val activity: ComponentActivity) : SingleFilePicker {
    private var scope: CoroutineScope? = null
    private val permission by lazy { AndroidPickerPermissionManager(activity, scope) }
    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private val results by lazy { Channel<Uri?>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            scope?.launch { results.send(uri) }
        }
        permission.register()
    }

    private suspend fun show(
        mimes: List<Mime>,
        limit: MemorySize,
    ): SinglePickerResponse {
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        l.launch(mimes.map { it.text }.toTypedArray())
        val resp = results.receive()?.let { LocalFile(it) } ?: return Cancelled
        val errors = FileInfo(activity, resp).fits(mimes, limit)
        if (errors.isEmpty()) return FilePicked(resp)
        return Failure(errors)
    }

    override suspend fun open(
        mimes: List<Mime>,
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