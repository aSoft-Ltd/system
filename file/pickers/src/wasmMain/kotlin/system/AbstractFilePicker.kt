package system

import kotlinx.browser.window
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileList
import system.file.PickerLimit
import system.file.PickingException
import system.file.mime.All
import system.file.mime.Mime
import system.file.picker.response.Cancelled
import system.file.toResponse
import system.internal.BrowserFileInfo
import system.internal.LocalFileImpl
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class AbstractFilePicker {
    protected fun input() = window.document.createElement("input").apply {
        setAttribute("type", "file")
    } as HTMLInputElement

    protected fun FileList.toList(): List<LocalFileImpl> = buildList {
        for (i in 0 until length) {
            val file = item(i) ?: continue
            add(LocalFileImpl(file))
        }
    }
}