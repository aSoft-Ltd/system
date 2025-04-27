package system.file.picker.response

import system.LocalFile
import system.file.PickingException

sealed interface SinglePickerResponse

sealed interface MultiPickerResponse

data object Cancelled : SinglePickerResponse, MultiPickerResponse

data object Denied : SinglePickerResponse, MultiPickerResponse

data class FilePicked(val file: LocalFile) : SinglePickerResponse

data class Failure(val errors: List<PickingException>) : SinglePickerResponse, MultiPickerResponse, List<PickingException> by errors {
    fun toException() = Exception(errors.joinToString("\n") { it.message })
}

data class FilesPicked(val files: List<LocalFile>) : MultiPickerResponse, List<LocalFile> by files