package system

import system.internal.JvmFileReader

actual fun SystemFileReader(): FileReader = JvmFileReader()