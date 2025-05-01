package system.file

import system.FileInfo
import system.LocalFile
import system.MemorySize
import system.file.mime.Mime
import system.file.picker.response.Cancelled
import system.file.picker.response.Failure
import system.file.picker.response.FilesPicked
import system.file.picker.response.MultiPickerResponse

private suspend fun FileInfo.fits(mimes: List<Mime>, limit: MemorySize): List<PickingException> = buildList {
    val mime = Mime.from(extension())
    val name = name()
    if (mimes.none { it.matches(mime) }) add(PickingException.InvalidMimeType(name, mime, mimes))
    val size = size()
    if (size > limit) add(PickingException.SizeLimitExceeded(name, size, limit))
}

internal suspend fun List<LocalFile>.toResponse(
    mimes: List<Mime>,
    limit: PickerLimit,
    infos: List<FileInfo>
): MultiPickerResponse {
    if (isEmpty()) return Cancelled
    val errors = buildList {
        if (size > limit.count) {
            add(PickingException.CountLimitExceeded(size, limit.count))
        }
        for (file in infos) {
            addAll(file.fits(mimes, limit.size))
        }
    }
    if (errors.isNotEmpty()) return Failure(errors)
    return FilesPicked(this)
}