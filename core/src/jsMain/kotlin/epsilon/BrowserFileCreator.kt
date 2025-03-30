package epsilon

import org.w3c.files.Blob
import org.w3c.files.File
import org.w3c.files.FilePropertyBag

class BrowserFileCreator : FileCreator {

    override fun binary(
        content: ByteArray,
        name: String,
        type: String
    ): RawFile = File(arrayOf(content), name, FilePropertyBag(type = type))

    override fun text(
        content: String,
        name: String,
        type: String
    ): RawFile = File(arrayOf(content), name, FilePropertyBag(type = type))

    fun from(
        blob: Blob,
        name: String,
        type: String = "application/octet-stream"
    ): RawFile = File(arrayOf(blob), name, FilePropertyBag(type = type))
}