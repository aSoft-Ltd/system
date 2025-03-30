@file:JsExport

package epsilon

import epsilon.MemoryUnit.*
import epsilon.serializers.MemorySizeSerializer
import kotlinx.JsExport
import kotlinx.JsName
import kotlinx.serialization.Serializable
import kotlin.math.round

@Serializable(with = MemorySizeSerializer::class)
data class MemorySize(
    val value: Double,
    val multiplier: Multiplier,
    val unit: MemoryUnit
) {
    override fun toString() = "${value.toString().removeSuffix(".0")} ${multiplier}${unit}"

    private val convertor by lazy {
        when (unit) {
            Bits -> ::convertBits
            Bytes -> ::convertBytes
        }
    }

    operator fun minus(other: MemorySize): MemorySize {
        val lhs = this
        val rhs = other.to(lhs.unit).to(lhs.multiplier)
        return MemorySize(lhs.value - rhs.value, lhs.multiplier, lhs.unit)
    }

    operator fun div(other: MemorySize): Double {
        val num = this
        val den = other.to(num.unit).to(num.multiplier)
        return num.value / den.value
    }

    fun toBestSize(): MemorySize {
        val multipliers = Multiplier.values().reversed()
        var best: Double
        for (multiplier in multipliers) {
            best = convertor(value, this.multiplier, multiplier)
            if (best >= 1) {
                return MemorySize(round(best * 100) / 100, multiplier, unit)
            }
        }
        return this
    }

    fun toBytes(): MemorySize {
        if (unit == Bytes) return this
        return MemorySize(value / 8, multiplier, Bytes)
    }

    fun toBits(): MemorySize {
        if (unit == Bits) return this
        return MemorySize(value * 8, multiplier, Bits)
    }

    fun to(unit: MemoryUnit): MemorySize = when (unit) {
        Bits -> toBits()
        Bytes -> toBytes()
    }

    @JsName(name = "toMult")
    fun to(unit: Multiplier): MemorySize {
        val v = convertor(value, this.multiplier, unit)
        return MemorySize(v, unit, this.unit)
    }

    fun inBytes() = toBytes().to(Multiplier.Unit).value
}