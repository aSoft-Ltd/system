package system

import system.file.SingleMediaPicker
import system.file.mime.MediaMime
import system.file.picker.response.SinglePickerResponse

class BrowserSingleMediaPicker : AbstractSingleFilePicker(), SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize): SinglePickerResponse = show(mimes, limit)
}