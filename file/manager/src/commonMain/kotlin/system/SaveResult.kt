package system

sealed interface SaveResult {
    data object Success: SaveResult
    data object Cancelled: SaveResult
    data class Failure(val errors: List<Throwable>) : SaveResult
}