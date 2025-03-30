package epsilon

fun memorySize(text: String?): MemorySize {
    if (text == null) throw NullPointerException("memory size argument is null")
    var raw = text.replace(" ", "")
    val unit = raw.lastOrNull()?.toUnit() ?: throw IllegalArgumentException("Invalid memory size unit in '$text'")
    raw = raw.dropLast(1)
    val multiplier = raw.lastOrNull()?.toMultiplier() ?: throw IllegalArgumentException("Invalid memory size multiplier in '$text'")
    val value = raw.toValidNumber()
    return MemorySize(value, multiplier, unit)
}

fun memorySizeOrNull(text: String?): MemorySize? = try {
    memorySize(text)
} catch (e: Throwable) {
    null
}

val Number.bytes get() = MemorySize(toDouble(), Multiplier.Unit, MemoryUnit.Bytes)

private fun String.toValidNumber(): Double {
    var raw = ""
    for (index in indices) {
        raw += when (val char = this[index]) {
            in '0'..'9' -> char
            '.' -> char
            in listOf('E', 'e') -> char
            else -> break
        }
    }
    if (raw.isBlank()) throw IllegalArgumentException("$this can't be converted into a number")
    return raw.toDouble()
}

private fun Char.toUnit(): MemoryUnit = when (this) {
    'B' -> MemoryUnit.Bytes
    'b' -> MemoryUnit.Bits
    else -> throw IllegalArgumentException("Invalid memory unit '$this'")
}

private fun Char.toMultiplier() = when (this) {
    in listOf('K', 'k') -> Multiplier.Kilo
    in listOf('M', 'm') -> Multiplier.Mega
    in listOf('G', 'g') -> Multiplier.Giga
    in listOf('T', 't') -> Multiplier.Tera
    in listOf('P', 'p') -> Multiplier.Peta
    in listOf('E', 'e') -> Multiplier.Exa
    in listOf('Z', 'z') -> Multiplier.Zetta
    in listOf('Y', 'y') -> Multiplier.Yotta
    else -> Multiplier.Unit
}