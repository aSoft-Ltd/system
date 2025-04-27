package system

import system.file.SingleFilePicker
import system.file.mime.Mime
import system.file.picker.response.SinglePickerResponse

class BrowserSingleFilePicker : AbstractSingleFilePicker(), SingleFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: MemorySize): SinglePickerResponse = show(mimes, limit)
}