@file:Suppress("NOTHING_TO_INLINE")

package epsilon

import epsilon.internal.FileBlobImpl
import kase.Result
import kase.catching
@JsName("fileBlobAt")
actual inline fun FileBlob(path: String?): Result<FileBlob> = catching {
    FileBlobImpl.from(path ?: throw IllegalArgumentException("path must not be null"))
}