package system.file.picker.response

import system.file.PickingException

fun MultiPickerResponse.toSingle(): SinglePickerResponse = when (this) {
    is Cancelled -> this
    is Denied -> this
    is Failure -> this
    is FilesPicked -> when (val count = files.size) {
        0 -> Cancelled
        1 -> FilePicked(files.first())
        else -> Failure(errors = listOf(PickingException.CountLimitExceeded(count, 1)))
    }
}