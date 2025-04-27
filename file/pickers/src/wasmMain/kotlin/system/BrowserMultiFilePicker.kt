package system

import system.file.MultiFilePicker
import system.file.PickerLimit
import system.file.mime.Mime
import system.file.picker.response.MultiPickerResponse

class BrowserMultiFilePicker : AbstractMultiFilePicker(), MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse = show(mimes, limit)
}