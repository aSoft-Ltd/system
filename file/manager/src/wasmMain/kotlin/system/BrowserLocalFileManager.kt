package system

import system.file.FilePickers
import system.internal.BrowserFileInfo
import system.internal.BrowserFileReader
import system.internal.BrowserFileSaver
import system.internal.LocalFileImpl
import system.FileReader as MppFileReader

class BrowserLocalFileManager :
    LocalFileManager,
    MppFileReader by BrowserFileReader(),
    FileSaver by BrowserFileSaver() {

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
}