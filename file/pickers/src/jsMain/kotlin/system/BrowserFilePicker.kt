package system

import koncurrent.Later
import kotlinx.browser.window
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileList
import system.file.mime.All
import system.file.mime.Mime
import system.internal.LocalFileImpl
import system.permissions.VirtualFilePickerPermissionManager
import system.picker.files.FilePicker

class BrowserFilePicker : FilePicker {

    override val permission by lazy { VirtualFilePickerPermissionManager(Permission.Granted) }

    override fun openFileChooser(mimes: List<Mime>, multiple: Boolean) = Later { resolve, _ ->
        val input = window.document.createElement("input").apply {
            setAttribute("type", "file")
            if (mimes.contains(All)) {
                setAttribute("accept", "*/*")
            } else {
                setAttribute("accept", mimes.joinToString(",") { it.text })
            }
            if (multiple) setAttribute("multiple", "")
        } as HTMLInputElement
        input.oncancel = { resolve(emptyList()) }
        input.onchange = { resolve(input.files?.toList() ?: emptyList()) }
        input.click()
    }

    private fun FileList.toList(): List<LocalFile> = buildList {
        for (i in 0 until length) {
            val file = item(i) ?: continue
            add(LocalFileImpl(file))
        }
    }
}