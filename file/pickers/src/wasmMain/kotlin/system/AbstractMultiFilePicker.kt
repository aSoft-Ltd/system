package system

import system.file.PickerLimit
import system.file.PickingException
import system.file.mime.All
import system.file.mime.Mime
import system.file.picker.response.Cancelled
import system.file.toResponse
import system.internal.BrowserFileInfo
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class AbstractMultiFilePicker : AbstractFilePicker() {

    suspend fun show(mimes: List<Mime>, limit: PickerLimit) = suspendCoroutine { cont ->
        val input = input().apply {
            if (mimes.contains(All)) {
                setAttribute("accept", "*/*")
            } else {
                setAttribute("accept", mimes.joinToString(",") { it.text })
            }
            setAttribute("multiple", "true")
        }
        input.oncancel = { cont.resume(Cancelled) }
        input.onchange = {
            val files = input.files?.toList() ?: emptyList()
            if (files.isEmpty()) {
                cont.resume(Cancelled)
            } else {
                val errors = buildList {
                    if (files.size > limit.count) {
                        add(PickingException.CountLimitExceeded(files.size, limit.count))
                    }
                    for (file in files.map { BrowserFileInfo(it) }) {
                        val mime = Mime.from(file.extension())
                        if (mimes.none { it.matches(mime) }) add(PickingException.InvalidMimeType(file.name(), mime, mimes))
                        val size = file.size()
                        if (size > limit.size) add(PickingException.SizeLimitExceeded(file.name(), size, limit.size))
                    }
                }
                cont.resume(files.toResponse(errors))
            }
        }
        input.click()
    }
}