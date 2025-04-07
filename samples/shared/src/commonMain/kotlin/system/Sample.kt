package system

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun Sample(
    files: FileManager
) {
    Column {
        FilePicker(files)
        ImagePicker(files)
    }
}