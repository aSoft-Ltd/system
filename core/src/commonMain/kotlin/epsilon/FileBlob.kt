@file:JsExport

package epsilon

import kotlinx.JsExport

@Deprecated("In favour of RawFile")
interface FileBlob : Blob {
    val path: String
    val name: String
}