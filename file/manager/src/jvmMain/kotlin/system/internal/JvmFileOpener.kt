package system.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import system.FileOpener
import system.LocalFile
import system.file.response.Failure
import system.file.response.FileReturned
import system.file.response.ResponseError
import system.file.response.SingleFileResponse
import java.awt.Desktop
import java.io.File

class JvmFileOpener : FileOpener {

    override suspend fun open(file: LocalFile) : SingleFileResponse = when(file) {
        is LocalFileImpl -> open(file.path)
        is FileReturned -> open(file.file)
        else -> Failure(errors = listOf(ResponseError.UnknownFileType(file)))
    }

    override suspend fun open(url: String): SingleFileResponse = withContext(Dispatchers.IO) {
        Desktop.getDesktop().open(File(url))
        FileReturned(LocalFileImpl(url))
    }
}