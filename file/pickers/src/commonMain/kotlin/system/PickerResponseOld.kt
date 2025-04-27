package system

sealed interface PickerResponseOld {
    data class Picked(private val files: List<LocalFile>) : PickerResponseOld, List<LocalFile> by files
    data object Denied : PickerResponseOld
    data object Cancelled : PickerResponseOld
}