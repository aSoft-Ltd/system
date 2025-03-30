package epsilon

import epsilon.internal.filename
import koncurrent.Executor
import koncurrent.Later
import koncurrent.later.then
import koncurrent.toLater
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLAnchorElement
import org.w3c.fetch.RequestInit
import org.w3c.files.FileReader
import status.Progress
import kotlin.js.json

class BrowserFileManager : FileManager {

    override val create by lazy { BrowserFileCreator() }

    override fun open(url: String): Later<String> = window.open(url, "_blank").toLater().then {
        if (it == null) throw Exception("Failed to open $url") else url
    }

    override fun download(url: String, name: String?, headers: Map<String, String>): Later<RawFile> {
        val filename = name ?: url.filename() ?: "file.unknown"
        val ri = RequestInit(headers = json(*headers.map { it.key to it.value }.toTypedArray()))
        return window.fetch(url, ri).then {
            it.blob()
        }.then {
            create.from(it, filename)
        }.unsafeCast<Later<RawFile>>()
    }

    override fun save(url: String, name: String?): Later<String> {
        val filename = name ?: "file"
        return Later(url).then {
            val a = document.createElement("a") as HTMLAnchorElement
            a.href = url
            a.target = "_blank"
            a.download = filename
            document.body?.appendChild(a)
            a
        }.then {
            it.click()
            document.body?.removeChild(it)
            url
        }
    }


    private val reader: FileReader = FileReader()

    override fun read(file: RawFile, executor: Executor, onProgress: ((Progress<MemorySize>) -> Unit)?): Later<ByteArray> = reader.readBytesOf(
        blob = file,
        executor = executor,
        onAbortMessage = "File reading of ${file.name} has been aborted",
        onErrorMessage = "Failed to read file: ${file.name}",
        onProgress = onProgress
    )
}