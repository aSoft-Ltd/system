package system.internal

import system.LocalFile
import system.readBytesOf
import koncurrent.Executor
import koncurrent.Later
import org.w3c.files.FileReader
import system.FileReader as MppFileReader

internal class BrowserFileReader : MppFileReader {

    val reader: FileReader = FileReader()

    override fun read(file: LocalFile, executor: Executor): Later<ByteArray> = reader.readBytesOf(
        blob = file,
        executor = executor,
        actionName = "Reading ${file.name}",
        onAbortMessage = "File reading of ${file.name} has been aborted",
        onErrorMessage = "Failed to read file: ${file.name}"
    )
}