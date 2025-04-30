package system.file

import system.FileInfo
import system.LocalFile
import system.MemorySize
import system.PickerResponseOld
import system.file.mime.Mime
import system.file.picker.response.Cancelled
import system.file.picker.response.Failure
import system.file.picker.response.FilesPicked
import system.file.picker.response.MultiPickerResponse

internal fun List<LocalFile>.toResponse(multiple: Boolean): PickerResponseOld {
    if (isEmpty()) return PickerResponseOld.Cancelled
    if (!multiple) return PickerResponseOld.Picked(take(1))
    return PickerResponseOld.Picked(this)
}

internal fun List<LocalFile>.toResponse(errors: List<PickingException>): MultiPickerResponse {
    if (errors.isNotEmpty()) return Failure(errors)
    if (isEmpty()) return Cancelled
    return FilesPicked(this)
}

suspend fun FileInfo.fits(mimes: List<Mime>, limit: MemorySize) : List<PickingException> = buildList {
    val mime = Mime.from(extension())
    val name = name()
    if (mimes.none { it.matches(mime) }) add(PickingException.InvalidMimeType(name, mime, mimes))
    val size = size()
    if (size > limit) add(PickingException.SizeLimitExceeded(name, size, limit))
}