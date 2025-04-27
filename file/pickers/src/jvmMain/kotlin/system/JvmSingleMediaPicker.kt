package system

import system.file.SingleMediaPicker
import system.file.internal.toMediaMimes
import system.file.mime.MediaMime

class JvmSingleMediaPicker : AbstractSingleFilePicker(), SingleMediaPicker {
    override suspend fun open(
        mimes: List<MediaMime>,
        limit: MemorySize
    ) = show(mimes.toMediaMimes(), limit)
}