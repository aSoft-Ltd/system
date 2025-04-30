package system

sealed interface PickerResponse {
    sealed interface Picked : PickerResponse
    sealed interface Denied : PickerResponse
    sealed interface Cancelled : PickerResponse
    sealed interface Failure : PickerResponse
}