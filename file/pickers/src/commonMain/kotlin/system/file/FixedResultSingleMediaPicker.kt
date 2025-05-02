package system.file

import system.MemorySize
import system.file.mime.MediaMime
import system.file.response.Denied
import system.file.response.SingleFileResponse

class FixedResultSingleMediaPicker(private val result: Denied) : SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize): SingleFileResponse = result
}