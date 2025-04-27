package system.file

import system.LocalFile
import system.PickerResponseOld
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