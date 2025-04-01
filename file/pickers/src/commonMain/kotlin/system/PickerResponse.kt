package system

sealed interface PickerResponse {
    data class Picked(val files: List<LocalFile>) : PickerResponse
    data object Denied : PickerResponse
    data object Cancelled : PickerResponse
}