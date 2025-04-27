package system.file

import system.file.mime.Mime
import system.file.picker.response.MultiPickerResponse

class NoShowMultiFilePicker : MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse {
        TODO("Not yet implemented")
    }
}