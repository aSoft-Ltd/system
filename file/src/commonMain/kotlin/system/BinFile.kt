package system

expect fun BinFile(
    content: ByteArray = byteArrayOf(),
    name: String = "file.bin",
    type: String = "application/octet-stream"
): File