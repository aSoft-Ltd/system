package system

import androidx.compose.runtime.Composable

@Composable
fun Sample(
    files: FileManager
) {
    FilePicker(files)
}