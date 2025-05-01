package system

import system.file.FilePickers
import system.internal.FileInfoImpl
import system.internal.JvmFileReader
import system.internal.JvmFileSaver
import system.internal.LocalFileImpl
import java.io.File

class JvmLocalFileManager :
    LocalFileManager,
    FileReader by JvmFileReader(),
    FileSaver by JvmFileSaver() {
    override val pickers by lazy {
        FilePickers(
            documents = JvmMultiFilePicker(),
            document = JvmSingleFilePicker(),
            medias = JvmMultiMediaPicker(),
            media = JvmSingleMediaPicker(),
        )
    }

    override fun exists(file: LocalFile): Boolean = (file as? LocalFileImpl)?.let { File(it.path).exists() } ?: false

    override fun info(file: LocalFile): FileInfo = FileInfoImpl(file as LocalFileImpl)
}