package system

import koncurrent.Later
import system.file.mime.All
import system.file.mime.Mime
import system.internal.LocalFileImpl
import system.permissions.VirtualFilePickerPermissionManager
import system.picker.files.FilePicker
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

class JavaFilePicker : FilePicker {
    override val permission by lazy { VirtualFilePickerPermissionManager(Permission.Granted) }

    override fun openFileChooser(
        mimes: List<Mime>,
        multiple: Boolean
    ) = Later<List<LocalFile>> { resolve, _ ->
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
        val response = if (result == JFileChooser.APPROVE_OPTION) {
            val files = if (multiple) chooser.selectedFiles.toList() else listOf(chooser.selectedFile)
            files.map { LocalFileImpl(it.path) }
        } else {
            emptyList()
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