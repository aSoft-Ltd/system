package system.file

import system.file.mime.Mime
import system.file.response.Denied
import system.file.response.MultiPickerResponse

class FixedResultMultiFilePicker(private val result: Denied) : MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse = result
}