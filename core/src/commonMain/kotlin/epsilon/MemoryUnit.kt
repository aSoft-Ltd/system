@file:JsExport
package epsilon

import kotlinx.JsExport
import kotlinx.serialization.Serializable
import kotlin.time.DurationUnit

@Serializable
enum class MemoryUnit {
    Bits, Bytes;
    override fun toString() : String = when(this) {
        Bits -> "b"
        Bytes -> "B"
    }
}