package system

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun FileSaver(files: FileSaver) {
    val scope = rememberCoroutineScope()
    Button(
        onClick = {
            scope.launch {
                files.save(content = "Saved from samples", name = "sample.txt")
            }
        }
    ) {
        Text("Save Text File")
    }
}