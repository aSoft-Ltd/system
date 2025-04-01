package system.file

import system.LocalFile
import system.PickerResponse

internal fun List<LocalFile>.toResponse(multiple: Boolean): PickerResponse {
    if (isEmpty()) return PickerResponse.Cancelled
    if (!multiple) return PickerResponse.Picked(take(1))
    return PickerResponse.Picked(this)
}