package system

import koncurrent.Executor
import koncurrent.Later
import koncurrent.TODOLater
import system.file.FilePickers
import system.internal.FileInfoImpl
import system.internal.LocalFileImpl
import java.io.File

class JvmFileManager : FileManager {
    override val pickers: FilePickers = FilePickers(documents = JvmFilePicker(), media = JvmFilePicker())

    override fun exists(file: LocalFile): Boolean = false

    override fun info(file: LocalFile): FileInfo = FileInfoImpl(file as LocalFileImpl)

    override fun open(file: LocalFile): Later<String> = TODOLater()

    override fun open(url: String): Later<String> = TODOLater()

    override fun save(file: LocalFile, name: String?): Later<String> = TODOLater()

    override fun read(file: LocalFile, executor: Executor): Later<ByteArray> = Later { resolve, reject ->
        try {
            file as LocalFileImpl
            val f = File(file.path)
            resolve(f.readBytes())
        } catch (e: Exception) {
            reject(e)
        }
    }
}