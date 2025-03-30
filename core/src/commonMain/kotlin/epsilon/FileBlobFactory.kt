package epsilon

import kase.Result

@Deprecated("In favour of RawFile")
expect inline fun FileBlob(path: String? = null): Result<FileBlob>