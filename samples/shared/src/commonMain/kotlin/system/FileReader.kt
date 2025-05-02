package system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import system.file.response.Cancelled
import system.file.response.Denied
import system.file.response.Failure
import system.file.response.FileReturned

@Composable
internal fun FileReader(files: LocalFileManager) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    Column {
        Row {
            Button(
                onClick = {
                    scope.launch {
                        when (val file = files.pickers.document.open()) {
                            is Cancelled -> {}
                            is Denied -> {}
                            is Failure -> {}
                            is FileReturned -> {
                                text = files.readText(file.file)
                            }
                        }
                    }
                }
            ) {
                Text("Read File")
            }

            Button(onClick = { text = "" }) {
                Text("Clear Text")
            }
        }
        Text(text = text)
    }
}