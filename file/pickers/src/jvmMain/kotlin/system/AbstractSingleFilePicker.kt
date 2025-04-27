package system

import kotlinx.coroutines.suspendCancellableCoroutine
import system.file.PickingException
import system.file.mime.Mime
import system.file.picker.response.Cancelled
import system.file.picker.response.Failure
import system.file.picker.response.FilePicked
import system.file.picker.response.SinglePickerResponse
import system.internal.toFileFilter
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

abstract class AbstractSingleFilePicker {

    private val chooser by lazy {
        JFileChooser().apply {
            fileSelectionMode = JFileChooser.FILES_ONLY
            isMultiSelectionEnabled = false
        }
    }

    protected suspend fun show(
        mimes: List<Mime>,
        limit: MemorySize
    ): SinglePickerResponse = suspendCancellableCoroutine { cont ->
        chooser.fileFilter = mimes.toFileFilter(limit)
        chooser.name = "Select File"
        SwingUtilities.invokeLater {
            val result = chooser.showOpenDialog(null)
            val response = when (result) {
                JFileChooser.APPROVE_OPTION -> {
                    val file = chooser.selectedFile
                    val size = file.length().bytes
                    val errors = buildList {
                        val mime = Mime.from(file.extension)
                        if (mimes.none { it.matches(mime) }) add(PickingException.InvalidMimeType(file.name, mime, mimes))
                        if (file.isDirectory) add(PickingException.FileIsDirectory(file.name))
                        if (size > limit) add(PickingException.SizeLimitExceeded(file.name, size, limit))
                    }
                    if (errors.isEmpty()) {
                        FilePicked(LocalFile(file.path))
                    } else {
                        Failure(errors)
                    }
                }

                else -> Cancelled
            }
            cont.resume(response)
        }
    }
}