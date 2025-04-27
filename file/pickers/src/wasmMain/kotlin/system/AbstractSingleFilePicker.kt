package system

import system.file.PickingException
import system.file.mime.All
import system.file.mime.Mime
import system.file.picker.response.Cancelled
import system.file.picker.response.Failure
import system.file.picker.response.FilePicked
import system.internal.BrowserFileInfo
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class AbstractSingleFilePicker : AbstractFilePicker() {
    suspend fun show(mimes: List<Mime>, limit: MemorySize) = suspendCoroutine { cont ->
        val input = input().apply {
            if (mimes.contains(All)) {
                setAttribute("accept", "*/*")
            } else {
                setAttribute("accept", mimes.joinToString(",") { it.text })
            }
        }
        input.oncancel = { cont.resume(Cancelled) }
        input.onchange = {
            val files = input.files?.toList() ?: emptyList()
            val response = if (files.isEmpty()) {
                Cancelled
            } else {
                val errors = buildList {
                    if (files.size > 1) {
                        add(PickingException.CountLimitExceeded(files.size, 1))
                    }
                    for (file in files.map { BrowserFileInfo(it) }) {
                        val mime = Mime.from(file.extension())
                        if (mimes.none { it.matches(mime) }) add(PickingException.InvalidMimeType(file.name(), mime, mimes))
                        val size = file.size()
                        if (size > limit) add(PickingException.SizeLimitExceeded(file.name(), size, limit))
                    }
                }
                if (errors.isNotEmpty()) {
                    Failure(errors)
                } else {
                    FilePicked(files.first())
                }
            }
            cont.resume(response)
        }
        input.click()
    }
}