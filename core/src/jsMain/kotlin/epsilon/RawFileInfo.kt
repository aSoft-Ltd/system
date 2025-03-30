@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package epsilon

import koncurrent.Later
import koncurrent.toLater
import org.w3c.dom.url.URL

actual class RawFileInfo actual constructor(actual val file: RawFile) {
    @Deprecated("in favour of nameWithExtension or nameWithoutExtension")
    actual val name = file.name

    actual val nameWithExtension by lazy { file.name }

    actual val nameWithoutExtension by lazy { file.name.substringBeforeLast(".") }

    actual val extension by lazy { file.name.substringAfterLast(".") }

    private var uri: String? = null

    actual val url: String
        get() {
            if (uri == null) {
                uri = URL.createObjectURL(file)
            }
            return uri!!
        }

    actual fun path() = paths.getOrPut(file) {
        URL.createObjectURL(file).toLater()
//        BrowserFileReader().reader.readDataUrl(
//            blob = file,
//            executor = Executors.default(),
//            actionName = "constructing url for ${file.name}",
//            onAbortMessage = "File reading of ${file.name} has been aborted",
//            onErrorMessage = "Failed to read file: ${file.name}"
//        )
    }

    actual fun dispose() {
        uri?.let { URL.revokeObjectURL(it) }
        uri = null
    }

    private companion object {
        val paths = mutableMapOf<RawFile, Later<String>>()
    }
}