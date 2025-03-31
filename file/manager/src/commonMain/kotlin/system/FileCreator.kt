package system

import koncurrent.Later

interface FileCreator {
    fun create(
        content: ByteArray = byteArrayOf(),
        directory: String? = null,
        name: String = "file.bin",
        type: String = "application/octet-stream"
    ): Later<LocalFile>

    fun create(
        content: String = "",
        directory: String? = null,
        name: String = "file.txt",
        type: String = "application/octet-stream"
    ): Later<LocalFile>
}