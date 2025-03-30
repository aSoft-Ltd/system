package epsilon

import koncurrent.Later

expect class RawFileInfo(file: RawFile) {
    val file: RawFile
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