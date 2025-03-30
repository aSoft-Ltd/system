package system

import koncurrent.Executor
import koncurrent.Executors
import koncurrent.Later

interface FileReader {
    fun read(file: File, executor: Executor = Executors.default()): Later<ByteArray>
}