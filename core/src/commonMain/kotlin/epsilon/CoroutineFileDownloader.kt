package epsilon

import epsilon.internal.filename
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.*
import koncurrent.Later
import koncurrent.later
import kotlinx.coroutines.CoroutineScope

class CoroutineFileDownloader(
    private val scope: CoroutineScope,
    private val http: HttpClient,
    private val creator: FileCreator
) : FileDownloader {

    override fun download(url: String, name: String?, headers: Map<String, String>): Later<RawFile> = scope.later {
        val response = http.get(url) { headers.forEach { (key, value) -> header(key, value) } }
        val content = response.bodyAsChannel().toByteArray()
        val filename = name ?: url.filename() ?: "file.bin"
        creator.binary(content,filename, "application/octet-stream")
    }
}