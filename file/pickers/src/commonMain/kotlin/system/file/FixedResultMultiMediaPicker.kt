package system.file

import system.file.mime.MediaMime
import system.file.picker.response.Denied
import system.file.picker.response.MultiPickerResponse

class FixedResultMultiMediaPicker(private val result: Denied) : MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse = result
}