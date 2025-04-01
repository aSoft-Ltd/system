package system

import koncurrent.Later
import system.file.FilePicker
import system.file.mime.All
import system.file.mime.Mime
import system.file.toResponse
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

class JavaFilePicker : FilePicker {
    override fun openPicker(
        mimes: List<Mime>,
        multiple: Boolean
    ) = Later { resolve, _ ->
        val chooser = JFileChooser().apply {
            isMultiSelectionEnabled = multiple
            fileSelectionMode = JFileChooser.FILES_ONLY
            fileFilter = object : FileFilter() {
                override fun accept(p0: File?): Boolean = if (p0 == null) {
                    false
                } else if (mimes.contains(All)) {
                    true
                } else {
                    mimes.any { it.matches(p0.extension) }
                }

                override fun getDescription(): String = mimes.map { it.name }.firstOrNull() ?: "All files"
            }
        }

        val result = chooser.showOpenDialog(null)
        val response = when (result) {
            JFileChooser.APPROVE_OPTION -> {
                val files = if (multiple) chooser.selectedFiles.toList() else listOf(chooser.selectedFile)
                files.map { LocalFile(it.path) }.toResponse(multiple)
            }

            else -> PickerResponse.Cancelled
        }
        resolve(response)
    }

//    override fun openDirChooser() = Later<LocalFile?> { resolve, _ ->
//        val chooser = JFileChooser().apply {
//            isMultiSelectionEnabled = false
//            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
//        }
//        val result = chooser.showOpenDialog(null)
//        if (result == JFileChooser.CANCEL_OPTION) {
//            resolve(null)
//        } else {
//            resolve(chooser.selectedFile?.let { LocalFileImpl(it.path) })
//        }
//    }
}