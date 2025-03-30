@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package epsilon

import epsilon.internal.FileBlobImpl
import kase.Result
import kase.catching
import koncurrent.Executor
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.Blob as BBlob

class BrowserBlob(val blob: BBlob) : Blob {

    private val reader by lazy { FileReader() }

    override fun readBytes(executor: Executor) = reader.readBytesOf(
        blob = blob,
        executor = executor,
        actionName = "Reading blob",
        onAbortMessage = "Reading of blob has been aborted",
        onErrorMessage = "Failed to read file blob"
    )

    fun toFileBlob(name: String): Result<FileBlob> = catching {
        FileBlobImpl(blob.unsafeCast<File>(), name)
    }
}