package system.internal

import system.MemorySize
import system.bytes
import system.file.mime.All
import system.file.mime.Mime
import java.io.File
import javax.swing.filechooser.FileFilter

internal class MimeFileFilter(
    private val mimes: List<Mime>,
    private val limit: MemorySize,
) : FileFilter() {
    override fun accept(p0: File?): Boolean = if (p0 == null) {
        false
    } else if (mimes.contains(All)) {
        true
    } else {
        mimes.any { it.matches(p0.extension) } && p0.length().bytes <= limit
    }

    override fun getDescription(): String = mimes.map { it.name }.firstOrNull() ?: "All files"
}