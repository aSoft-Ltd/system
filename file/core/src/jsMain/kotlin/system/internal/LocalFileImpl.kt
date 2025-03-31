package system.internal

import org.w3c.files.File
import system.LocalFile

data class LocalFileImpl(private val handler: File): LocalFile