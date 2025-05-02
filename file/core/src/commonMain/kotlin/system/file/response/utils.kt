package system.file.response

fun MultiPickerResponse.toSingle(): SingleFileResponse = when (this) {
    is Cancelled -> this
    is Denied -> this
    is Failure -> this
    is FilesReturned -> when (val count = files.size) {
        0 -> Cancelled
        1 -> FileReturned(files.first())
        else -> Failure(errors = listOf(ResponseError.CountLimitExceeded(count, 1)))
    }
}