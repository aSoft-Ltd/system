package system.file

import system.MemorySize
import system.file.mime.Mime

sealed interface PickingException {
    val message: String

    class InvalidMimeType(
        val file: String,
        val mime: Mime,
        val allowed: List<Mime>
    ) : PickingException {
        override val message by lazy {
            "File $file of mime $mime does not match the allowed mime types ($allowed)"
        }
    }

    class FileIsDirectory(
        val file: String
    ) : PickingException {
        override val message by lazy { "File $file is a directory" }
    }

    class SizeLimitExceeded(
        val file: String,
        val size: MemorySize,
        val limit: MemorySize
    ) : PickingException {
        override val message by lazy {
            "File $file with ${size.toBestSize()} exceeds the limit of $limit"
        }
    }

    class CountLimitExceeded(
        val count: Int,
        val limit: Int
    ) : PickingException {
        override val message by lazy {
            "You can only select up to $limit files but you selected $count files"
        }
    }

    fun toException() = Exception(message)
}