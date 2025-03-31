package system

import koncurrent.Executor
import koncurrent.Executors
import koncurrent.Later

interface FileReader {
    fun read(file: LocalFile, executor: Executor = Executors.default()): Later<ByteArray>
}