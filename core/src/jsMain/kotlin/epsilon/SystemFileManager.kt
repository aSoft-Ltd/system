package epsilon

actual fun SystemFileManager(): FileManager = BrowserFileManager()