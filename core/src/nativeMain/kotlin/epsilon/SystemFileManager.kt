package epsilon

import epsilon.internal.UnImplementedFileManager

actual fun SystemFileManager(): FileManager = UnImplementedFileManager()