package system.internal

import koncurrent.Later
import kotlinx.browser.window
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileList
import system.FileChooser
import system.LocalFile
import system.Permission
import system.permissions.VirtualFilePickerPermissionManager

class BrowserFileChooser : FileChooser {

    override val permission by lazy { VirtualFilePickerPermissionManager(Permission.Granted) }

    override fun openFileChooser(extensions: List<String>, multiple: Boolean) = Later { resolve, _ ->
        val input = window.document.createElement("input").apply {
            setAttribute("type", "file")
            setAttribute("accept", extensions.joinToString(",") { "*.$it" })
            if (multiple) setAttribute("multiple", "")
        } as HTMLInputElement
        input.oncancel = { resolve(emptyList()) }
        input.onchange = { resolve(input.files?.toList() ?: emptyList()) }
        input.click()
    }

    override fun openDirChooser() = Later { resolve, _ ->
        val input = window.document.createElement("input").apply {
            setAttribute("type", "file")
        } as HTMLInputElement
        input.oncancel = { resolve(null) }
        input.onchange = { resolve(null) }
        input.click()
    }

    private fun FileList.toList(): List<LocalFile> = buildList {
        for (i in 0 until length) {
            val file = item(i) ?: continue
            add(LocalFileImpl(file))
        }
    }
}