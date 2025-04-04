package system

import koncurrent.Later
import system.file.FilePicker
import system.file.mime.All
import system.file.mime.Mime
import system.file.toResponse
import java.awt.Component
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import javax.swing.filechooser.FileFilter

class JvmFilePicker(private var parent: Component?) : FilePicker {

    private val chooser by lazy {
        JFileChooser().apply {
            fileSelectionMode = JFileChooser.FILES_ONLY
        }
    }

    override fun openPicker(
        mimes: List<Mime>,
        multiple: Boolean
    ) = Later { resolve, _ ->
        chooser.isMultiSelectionEnabled = multiple
        chooser.fileFilter = object : FileFilter() {
            override fun accept(p0: File?): Boolean = if (p0 == null) {
                false
            } else if (mimes.contains(All)) {
                true
            } else {
                mimes.any { it.matches(p0.extension) }
            }

            override fun getDescription(): String = mimes.map { it.name }.firstOrNull() ?: "All files"
        }
        SwingUtilities.invokeLater {
            val result = chooser.showOpenDialog(parent)
            val response = when (result) {
                JFileChooser.APPROVE_OPTION -> {
                    val files = if (multiple) chooser.selectedFiles.toList() else listOf(chooser.selectedFile)
                    files.map { LocalFile(it.path) }.toResponse(multiple)
                }

                else -> PickerResponse.Cancelled
            }
            resolve(response)
        }
    }
}