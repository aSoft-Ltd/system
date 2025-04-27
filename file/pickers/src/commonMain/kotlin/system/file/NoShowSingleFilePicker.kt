package system.file

import system.MemorySize
import system.file.mime.Mime
import system.file.picker.response.SinglePickerResponse

class NoShowSingleFilePicker : SingleFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: MemorySize): SinglePickerResponse {
        TODO("Not yet implemented")
    }
}