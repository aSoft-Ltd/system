package epsilon.internal

import epsilon.FileBlob
import epsilon.FileNotFoundException
import epsilon.readBytesOf
import koncurrent.Executor
import org.w3c.dom.url.URL
import org.w3c.files.File
import org.w3c.files.FileReader

@PublishedApi
internal data class FileBlobImpl(val file: File, override val name: String) : FileBlob {
    constructor(file: File) : this(file, file.name)

    override val path = getOrCreateUrl()

    private val reader by lazy { FileReader() }

    override fun readBytes(executor: Executor) = reader.readBytesOf(
        blob = file,
        executor = executor,
        actionName = "Reading ${file.name}",
        onAbortMessage = "File reading of ${file.name} has been aborted",
        onErrorMessage = "Failed to read file: ${file.name}"
    )

    override fun toString() = "File(name=$name, path=$path)"

    companion object {
        private val map = mutableMapOf<FileBlobImpl, String>()

        private fun FileBlobImpl.getOrCreateUrl() = map.getOrPut(this) { URL.createObjectURL(file) }

        fun from(path: String): FileBlob {
            val exception = FileNotFoundException(path)
            if (!map.containsValue(path)) throw exception
            return map.firstNotNullOfOrNull { (file, url) ->
                if (url == path) file else null
            } ?: throw exception
        }
    }
}