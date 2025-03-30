@file:JvmName("FileBlobConstructorsJvm")
@file:Suppress("NOTHING_TO_INLINE")

package epsilon

import epsilon.internal.FileBlobImpl
import kase.Result

actual inline fun FileBlob(path: String?): Result<FileBlob> = Result(path).map { FileBlobImpl.of(it) }