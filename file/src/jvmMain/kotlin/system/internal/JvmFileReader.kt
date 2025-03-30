package system.internal

import system.FileReader
import system.File
import koncurrent.Executor
import koncurrent.Later

internal class JvmFileReader : FileReader {
    override fun read(file: File, executor: Executor): Later<ByteArray> = Later(executor) { resolve, reject ->
        try {
            resolve(file.readBytes())
        } catch (err: Throwable) {
            reject(err)
        }
    }
}