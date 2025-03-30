@file:Suppress("NOTHING_TO_INLINE")

package epsilon

import epsilon.internal.MemorySizeProgress
import status.Progress

inline fun Progress(
    done: MemorySize,
    total: MemorySize
) : Progress<MemorySize> = MemorySizeProgress(done,total)