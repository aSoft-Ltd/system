package system

import system.file.SingleFilePicker
import system.file.mime.Mime

class JvmSingleFilePicker : AbstractSingleFilePicker(), SingleFilePicker {
    override suspend fun open(
        mimes: List<Mime>,
        limit: MemorySize
    ) = show(mimes, limit)
}