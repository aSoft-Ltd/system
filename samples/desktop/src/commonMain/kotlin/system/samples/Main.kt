package system.samples

import androidx.compose.ui.window.singleWindowApplication
import system.JvmFilePicker
import system.Sample

fun main() {
    singleWindowApplication {
        Sample(images = JvmFilePicker(window))
    }
}