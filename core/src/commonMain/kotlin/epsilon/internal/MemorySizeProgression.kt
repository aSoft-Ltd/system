package epsilon.internal

import epsilon.MemorySize
import kase.progress.ProgressionSplit
import kase.progress.SimpleProgression
import kotlin.math.round

@PublishedApi
internal class MemorySizeProgression(
    done: MemorySize,
    total: MemorySize
) : SimpleProgression<MemorySize> {
    override val done by lazy { done.toBestSize() }
    override val total by lazy { total.toBestSize() }
    override val left by lazy { this.total - this.done }
    override val fraction by lazy {
        val value = round(100 * if (this.total.value == 0.0) 0.0 else (this.done / this.total)) / 100
        ProgressionSplit(value, 1 - value)
    }
    override val percentage by lazy { this.fraction * 100 }
}