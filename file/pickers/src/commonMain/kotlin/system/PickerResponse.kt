package system

sealed interface PickerResponse {
    data class Picked(private val files: List<LocalFile>) : PickerResponse, List<LocalFile> by files
    data object Denied : PickerResponse
    data object Cancelled : PickerResponse
}