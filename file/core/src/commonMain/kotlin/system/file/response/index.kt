package system.file.response

import system.LocalFile

sealed interface SingleFileResponse

sealed interface MultiPickerResponse

data object Cancelled : SingleFileResponse, MultiPickerResponse

data object Denied : SingleFileResponse, MultiPickerResponse

data class FileReturned(val file: LocalFile) : SingleFileResponse, LocalFile by file

data class Failure(val errors: List<ResponseError>) : SingleFileResponse, MultiPickerResponse, List<ResponseError> by errors {
    fun toException() = Exception(errors.joinToString("\n") { it.message })
}

data class FilesReturned(val files: List<LocalFile>) : MultiPickerResponse, List<LocalFile> by files