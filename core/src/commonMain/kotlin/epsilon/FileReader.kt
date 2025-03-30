package epsilon

import koncurrent.Executor
import koncurrent.Executors
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import status.Progress

interface FileReader {
    fun read(file: RawFile, executor: Executor = Executors.default(), onProgress: ((Progress<MemorySize>) -> Unit)? = null): Later<ByteArray>
}