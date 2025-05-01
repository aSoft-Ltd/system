package system

import koncurrent.Executor
import koncurrent.Later
import koncurrent.TODOLater
import org.w3c.files.FileReader
import system.file.FilePickers
import system.internal.BrowserFileInfo
import system.internal.LocalFileImpl

class BrowserLocalFileManager : LocalFileManager {
    override val pickers by lazy {
        FilePickers(
            documents = BrowserMultiFilePicker(),
            document = BrowserSingleFilePicker(),
            medias = BrowserMultiMediaPicker(),
            media = BrowserSingleMediaPicker()
        )
    }

    override fun exists(file: LocalFile): Boolean = true

    override fun info(file: LocalFile): FileInfo = BrowserFileInfo(file as LocalFileImpl)

    override fun open(file: LocalFile): Later<String> = TODOLater()

    override fun open(url: String): Later<String> = TODOLater()

    override fun save(file: LocalFile, name: String?): Later<String> = TODOLater()

    override fun readBytes(file: LocalFile, executor: Executor): Later<ByteArray> {
        val reader = FileReader()
        file as LocalFileImpl
        return reader.readBytesOf(
            blob = file.wrapped,
            executor = executor,
            actionName = "Reading ${file.wrapped.name}",
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        )
    }
}