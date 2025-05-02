package system

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun FileSaver(files: FileSaver) {
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf<String?>(null) }
    Button(
        onClick = {
            scope.launch {
                message = "Saving file, please wait..."
                val result = files.save(content = "Saved from samples", name = "sample.txt")
                message = when (result) {
                    is SaveResult.Success -> "File saved successfully"
                    is SaveResult.Failure -> "Failed to save file: ${result.errors.joinToString(", ") { it.message ?: "" }}"
                    else -> "File save cancelled"
                }
                delay(3000)
                message = null
            }
        }
    ) {
        Text("Save Text File")
    }

    Text(message ?: "")
}