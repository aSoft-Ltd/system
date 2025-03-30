@file:Suppress("NOTHING_TO_INLINE")

package epsilon

import kase.Failure
import kase.Result

@PublishedApi
internal val nie = NotImplementedError("epsilon.FileBlob has not been implemented on all Native Targets")
actual inline fun FileBlob(path: String?): Result<FileBlob> = Failure(nie)