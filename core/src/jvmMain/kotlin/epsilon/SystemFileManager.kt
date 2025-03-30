package epsilon

actual fun SystemFileManager(): FileManager = JvmFileManager()