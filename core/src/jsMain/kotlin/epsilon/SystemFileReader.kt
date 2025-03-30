package epsilon

actual fun SystemFileReader(): FileReader = BrowserFileManager()