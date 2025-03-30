package system

import java.io.File

actual fun BinFile(
    content: ByteArray,
    name: String,
    type: String
): File {
    val tmp = System.getProperty("java.io.tmpdir") ?: "/tmp"
    return File(tmp, name).apply {
        createNewFile()
        writeBytes(content)
    }
}