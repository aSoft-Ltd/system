package system

import system.file.MultiMediaPicker
import system.file.PickerLimit
import system.file.mime.MediaMime
import system.file.picker.response.MultiPickerResponse

class BrowserMultiMediaPicker : AbstractMultiFilePicker(), MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse = show(mimes, limit)
}