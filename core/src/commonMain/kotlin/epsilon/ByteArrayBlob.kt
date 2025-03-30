package epsilon

import koncurrent.Executor
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch

@Deprecated("In favour of RawFile")
class ByteArrayBlob(val value: ByteArray) : Blob {
    override fun readBytes(executor: Executor): Later<ByteArray> = Later(value, executor)
}