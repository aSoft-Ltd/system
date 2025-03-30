package system

import koncurrent.Later

expect class FileInfo(file: File) {
    val file: File
    @Deprecated("use nameWithExtension or nameWithoutExtension")
    val name: String
    val nameWithExtension: String
    val nameWithoutExtension: String
    val extension: String
    val url: String

    @Deprecated("In favour of url")
    fun path(): Later<String>

    fun dispose()
}