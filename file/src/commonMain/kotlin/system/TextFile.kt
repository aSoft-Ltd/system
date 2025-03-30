package system

expect fun TextFile(
    content: String = "test content",
    name: String = "test.txt",
    type: String = "text/plain"
): File