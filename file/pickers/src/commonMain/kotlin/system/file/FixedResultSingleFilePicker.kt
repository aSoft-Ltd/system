package system.file

import system.MemorySize
import system.file.mime.Mime
import system.file.picker.response.Denied
import system.file.picker.response.SinglePickerResponse

class FixedResultSingleFilePicker(private val result: Denied) : SingleFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: MemorySize): SinglePickerResponse = result
}