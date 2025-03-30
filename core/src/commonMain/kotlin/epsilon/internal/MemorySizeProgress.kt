package epsilon.internal

import epsilon.MemorySize
import status.Progress
import status.ProgressionSplit
import kotlin.math.round

@PublishedApi
internal class MemorySizeProgress(
    override val done: MemorySize,
    override val total: MemorySize
) : Progress<MemorySize> {
    override val left by lazy { total - done }
    override val fraction by lazy {
        if (total.value == 0.0) return@lazy ProgressionSplit(0.0, 1.0)
        val value = (done / total).rounded()
        ProgressionSplit(value, 1 - value)
    }

    override val percentage by lazy { fraction * 100 }

    private fun Double.rounded() = round(this * 100) / 100
}