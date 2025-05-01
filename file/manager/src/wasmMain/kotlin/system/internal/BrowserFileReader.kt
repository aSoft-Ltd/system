package system.internal

import org.w3c.files.FileReader
import system.LocalFile
import system.readBytesOf
import system.readTextOf
import system.FileReader as MppFileReader

internal class BrowserFileReader : MppFileReader {

    val reader: FileReader by lazy { FileReader() }

    override suspend fun readBytes(file: LocalFile): ByteArray {
        file as LocalFileImpl
        return reader.readBytesOf(
            file.wrapped,
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        )
    }

    override suspend fun readText(file: LocalFile): String {
        file as LocalFileImpl
        return reader.readTextOf(
            file.wrapped,
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        )
    }
}