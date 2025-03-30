@file:Suppress("NOTHING_TO_INLINE")

package epsilon

import epsilon.internal.MemorySizeProgression
import kase.progress.SimpleProgression

inline fun Progression(
    done: MemorySize,
    total: MemorySize
): SimpleProgression<MemorySize> = MemorySizeProgression(done, total)