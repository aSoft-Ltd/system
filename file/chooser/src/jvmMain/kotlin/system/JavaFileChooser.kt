package system

import koncurrent.Later
import koncurrent.toLater
import system.internal.LocalFileImpl
import system.permissions.VirtualFilePickerPermissionManager
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

class JavaFileChooser : FileChooser {
    override val permission by lazy { VirtualFilePickerPermissionManager(Permission.Granted) }

    override fun openFileChooser(
        extensions: List<String>,
        multiple: Boolean
    ): Later<List<LocalFile>> {
        val fileChooser = JFileChooser().apply {
            isMultiSelectionEnabled = multiple
            fileSelectionMode = JFileChooser.FILES_ONLY
            fileFilter = object : FileFilter() {
                override fun accept(p0: File?): Boolean = if (extensions.contains("*")) true else ((p0?.extension ?: "") in extensions)
                override fun getDescription(): String = if (extensions.contains("*")) "All Files" else extensions.joinToString(", ") { "*.$it" }
            }
        }
        val result = fileChooser.showOpenDialog(null)
        return if (result == JFileChooser.APPROVE_OPTION) {
            val files = if (multiple) fileChooser.selectedFiles.toList() else listOf(fileChooser.selectedFile)
            files.map { LocalFileImpl(it.path) }
        } else {
            emptyList()
        }.toLater()
    }

    override fun openDirChooser(): Later<LocalFile?> {
        val fileChooser = JFileChooser().apply {
            isMultiSelectionEnabled = false
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        }
        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.CANCEL_OPTION) {
            return Later(null)
        }
        return Later(fileChooser.selectedFile?.let { LocalFileImpl(it.path) })
    }
}