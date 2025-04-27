package system.file

import system.file.mime.Mime
import system.file.picker.response.Denied
import system.file.picker.response.MultiPickerResponse

class DeniedMultiFilePicker : MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse = Denied
}