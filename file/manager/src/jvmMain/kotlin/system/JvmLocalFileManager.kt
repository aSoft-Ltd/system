package system

import system.file.FilePickers
import system.file.response.FileReturned
import system.internal.FileInfoImpl
import system.internal.JvmFileOpener
import system.internal.JvmFileReader
import system.internal.JvmFileSaver
import system.internal.LocalFileImpl
import system.internal.toFile
import system.internal.toFileOrNull
import java.io.File

class JvmLocalFileManager :
    LocalFileManager,
    FileReader by JvmFileReader(),
    FileSaver by JvmFileSaver(),
    FileOpener by JvmFileOpener() {
    override val pickers by lazy {
        FilePickers(
            documents = JvmMultiFilePicker(),
            document = JvmSingleFilePicker(),
            medias = JvmMultiMediaPicker(),
            media = JvmSingleMediaPicker(),
        )
    }

    override fun exists(file: LocalFile): Boolean = file.toFileOrNull()?.exists() ?: false

    override fun info(file: LocalFile): FileInfo = when (file) {
        is LocalFileImpl -> FileInfoImpl(file)
        is FileReturned -> info(file.file)
        else -> throw IllegalArgumentException("Unknown file type: $file")
    }
}