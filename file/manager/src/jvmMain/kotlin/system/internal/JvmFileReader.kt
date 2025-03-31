package system.internal

import koncurrent.Executor
import koncurrent.Later
import system.FileReader
import system.LocalFile
import java.io.File

internal class JvmFileReader : FileReader {
    override fun read(file: LocalFile, executor: Executor): Later<ByteArray> = Later(executor) { resolve, reject ->
        try {
            file as LocalFileImpl
            resolve(File(file.path).readBytes())
        } catch (err: Throwable) {
            reject(err)
        }
    }
}