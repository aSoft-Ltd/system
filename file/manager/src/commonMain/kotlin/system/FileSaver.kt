package system

import kase.Success
import system.file.mime.Application
import system.file.mime.Mime
import system.file.mime.Text

interface FileSaver {
    /**
     * Saves the file with [content] to the file system
     *
     * @return [Success] when the file was successfully saved
     * @return [Failure] when the file
     */
    suspend fun save(
        content: ByteArray = byteArrayOf(),
        name: String = "file.bin",
        type: Mime = Application.OctetStream
    ): SaveResult

    suspend fun save(
        content: String = "",
        name: String = "file.txt",
        type: Mime = Text.Plain
    ): SaveResult
}