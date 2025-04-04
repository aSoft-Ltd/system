package system.samples

import androidx.compose.ui.window.singleWindowApplication
import system.JvmFileManager
import system.Sample

fun main() {
    singleWindowApplication {
        Sample(files = JvmFileManager())
    }
}