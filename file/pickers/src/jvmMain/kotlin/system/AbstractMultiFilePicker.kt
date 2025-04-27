package system

import kotlinx.coroutines.suspendCancellableCoroutine
import system.file.PickerLimit
import system.file.PickingException
import system.file.mime.Mime
import system.file.picker.response.Cancelled
import system.file.picker.response.MultiPickerResponse
import system.file.toResponse
import system.internal.toFileFilter
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

abstract class AbstractMultiFilePicker {
    protected suspend fun show(
        mimes: List<Mime>,
        limit: PickerLimit
    ): MultiPickerResponse = suspendCancellableCoroutine { cont ->
        val chooser = JFileChooser().apply {
            fileSelectionMode = JFileChooser.FILES_ONLY
            isMultiSelectionEnabled = true
            fileFilter = mimes.toFileFilter(limit.size)
            name = "Select Files"
        }

        SwingUtilities.invokeLater {
            val result = chooser.showOpenDialog(null)
            val response = when (result) {
                JFileChooser.APPROVE_OPTION -> {
                    val files = chooser.selectedFiles
                    val errors = buildList {
                        if (files.size > limit.count) {
                            add(PickingException.CountLimitExceeded(files.size, limit.count))
                        }
                        for (file in files) {
                            val mime = Mime.from(file.extension)
                            if (mimes.none { it.matches(mime) }) add(PickingException.InvalidMimeType(file.name, mime, mimes))
                            if (file.isDirectory) add(PickingException.FileIsDirectory(file.name))
                            val size = file.length().bytes
                            if (size > limit.size) add(PickingException.SizeLimitExceeded(file.name, size, limit.size))
                        }
                    }
                    files.map { LocalFile(it.path) }.toResponse(errors)
                }

                else -> Cancelled
            }
            cont.resume(response)
        }
    }
}