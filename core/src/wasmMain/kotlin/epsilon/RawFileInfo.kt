package epsilon

import koncurrent.Later

actual class RawFileInfo actual constructor(actual val file: RawFile) {
    @Deprecated("in favour of nameWithExtension or nameWithoutExtension")
    actual val name : String get() = throw Throwable("Not implemented")

    actual val nameWithoutExtension: String get() = throw Throwable("Not implemented")

    actual val nameWithExtension: String get() = throw Throwable("Not implemented")

    actual val extension: String get() = throw Throwable("Not implemented")

    actual val url: String get() = throw Throwable("Not implemented")

    actual fun dispose() : Unit = throw Throwable("Not implemented")

    actual fun path() : Later<String> = throw Throwable("Not implemented")
}