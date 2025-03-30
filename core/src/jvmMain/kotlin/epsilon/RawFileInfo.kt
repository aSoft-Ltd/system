package epsilon

import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch

actual class RawFileInfo actual constructor(actual val file: RawFile) {
    @Deprecated("in favour of nameWithExtension or nameWithoutExtension")
    actual val name by lazy { file.name }

    actual val nameWithoutExtension by lazy { file.nameWithoutExtension }

    actual val nameWithExtension by lazy { file.name }

    actual val extension by lazy { file.extension }

    actual val url: String by lazy { file.absolutePath }

    actual fun path() = Later(file.absolutePath)

    actual fun dispose() {}
}