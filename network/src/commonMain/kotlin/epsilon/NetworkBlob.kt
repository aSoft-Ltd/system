package epsilon

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import koncurrent.Executor
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import koncurrent.later.then
import koncurrent.PendingLater
import koncurrent.later
import kotlinx.coroutines.CoroutineScope

class NetworkBlob(
    val url: String,
    val http: HttpClient,
    val scope: CoroutineScope,
    val builder: (suspend HttpRequestBuilder.() -> Unit)? = null
) : Blob {
    override fun readBytes(executor: Executor): Later<ByteArray> {
        val later = PendingLater<ByteArray>(executor)
        val (downloading) = later.setStages("Downloading $url")
        scope.later {
            val resp = http.get(url) { builder?.let { it() } }
            val channel = resp.bodyAsChannel()

            var offset = 0
            val byteBufferSize = 1024 * 100
            val contentLen = resp.contentLength() ?: 0L
            val data = ByteArray(contentLen.toInt())
            do {
                val currentRead = channel.readAvailable(data, offset, byteBufferSize)
                later.updateProgress(downloading(offset.toLong(), contentLen))
                offset += currentRead
            } while (currentRead >= 0)
            later.resolveWith(data)
        }
        return later
    }
}