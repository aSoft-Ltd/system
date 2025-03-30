package epsilon

interface FileCreator {

    fun binary(
        content: ByteArray = byteArrayOf(),
        name: String = "file.bin",
        type: String = "application/octet-stream"
    ): RawFile

    fun text(
        content: String = "",
        name: String = "file.txt",
        type: String = "text/plain"
    ): RawFile
}