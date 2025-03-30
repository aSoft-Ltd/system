package epsilon

import java.io.File

class JvmFileCreator(
    private val location: String = System.getProperty("java.io.tmpdir") ?: "/tmp"
) : FileCreator {

    override fun binary(
        content: ByteArray,
        name: String,
        type: String
    ): RawFile = File(location, name).apply {
        createNewFile()
        writeBytes(content)
    }

    override fun text(content: String, name: String, type: String): RawFile = File(location, name).apply {
        createNewFile()
        writeText(content)
    }
}