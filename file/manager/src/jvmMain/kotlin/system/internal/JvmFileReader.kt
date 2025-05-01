package system.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import system.FileReader
import system.LocalFile
import java.io.File

internal class JvmFileReader : FileReader {
    override suspend fun readBytes(file: LocalFile): ByteArray = withContext(Dispatchers.IO) {
        file as LocalFileImpl
        File(file.path).readBytes()
    }

    override suspend fun readText(file: LocalFile): String = withContext(Dispatchers.IO) {
        file as LocalFileImpl
        File(file.path).readText()
    }
}