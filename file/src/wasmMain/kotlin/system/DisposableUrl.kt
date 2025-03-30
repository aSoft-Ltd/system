package system

import org.w3c.dom.url.URL

actual data class DisposableUrl actual constructor(val value: String) {
    actual fun dispose() {
        URL.revokeObjectURL(value)
    }
}