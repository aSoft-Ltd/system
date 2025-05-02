package system.file

import system.MemorySize
import system.file.mime.Mime
import system.file.response.Denied
import system.file.response.SingleFileResponse

class FixedResultSingleFilePicker(private val result: Denied) : SingleFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: MemorySize): SingleFileResponse = result
}