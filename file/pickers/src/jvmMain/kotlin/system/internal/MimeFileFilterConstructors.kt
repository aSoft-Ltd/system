package system.internal

import system.MemorySize
import system.file.mime.Mime
import javax.swing.filechooser.FileFilter

internal fun List<Mime>.toFileFilter(limit: MemorySize): FileFilter = MimeFileFilter(this, limit)