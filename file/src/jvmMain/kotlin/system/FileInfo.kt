package system

import koncurrent.Later

actual class FileInfo actual constructor(actual val file: File) {
    @Deprecated("in favour of nameWithExtension or nameWithoutExtension")
    actual val name by lazy { file.name }

    actual val nameWithoutExtension by lazy { file.nameWithoutExtension }

    actual val nameWithExtension by lazy { file.name }

    actual val extension by lazy { file.extension }

    actual val url: String by lazy { file.absolutePath }

    actual fun path() = Later(file.absolutePath)

    actual fun dispose() {}
}