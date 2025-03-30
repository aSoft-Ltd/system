package system

import org.w3c.files.File
import org.w3c.files.FilePropertyBag

actual fun TextFile(
    content: String,
    name: String,
    type: String
): File = File(arrayOf(content), name, FilePropertyBag(type = type))