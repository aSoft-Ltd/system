package system.file

import system.MemorySize
import system.file.mime.MediaMime
import system.file.picker.response.Denied
import system.file.picker.response.SinglePickerResponse

class DeniedSingleMediaPicker : SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize): SinglePickerResponse = Denied
}