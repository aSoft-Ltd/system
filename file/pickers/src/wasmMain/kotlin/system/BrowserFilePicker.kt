package system

import koncurrent.Later
import kotlinx.browser.window
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileList
import system.file.FilePicker
import system.file.mime.All
import system.file.mime.Mime
import system.internal.LocalFileImpl

class BrowserFilePicker : FilePicker {

    override fun openPicker(mimes: List<Mime>, multiple: Boolean) = Later { resolve, _ ->
        val input = window.document.createElement("input").apply {
            setAttribute("type", "file")
            if (mimes.contains(All)) {
                setAttribute("accept", "*/*")
            } else {
                setAttribute("accept", mimes.joinToString(",") { it.text })
            }
            if (multiple) setAttribute("multiple", "")
        } as HTMLInputElement
        input.oncancel = { resolve(PickerResponse.Cancelled) }
        input.onchange = {
            val files = input.files?.toList() ?: emptyList()
            val response = when (files.isEmpty()) {
                true -> PickerResponse.Cancelled
                false -> PickerResponse.Picked(files)
            }
            resolve(response)
        }
        input.click()
    }

    private fun FileList.toList(): List<LocalFile> = buildList {
        for (i in 0 until length) {
            val file = item(i) ?: continue
            add(LocalFileImpl(file))
        }
    }
}