package system

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import koncurrent.later.then
import kotlinx.coroutines.launch
import system.file.FilePicker

@Composable
fun Sample(
    images: FilePicker
) {
    val scope = rememberCoroutineScope()
    var file = remember { mutableStateOf<LocalFile?>(null) }
    Button(
        onClick = {
            images.openPicker().then {
                println("Response: $it")
                when (it) {
                    PickerResponse.Cancelled -> println("Cancelled")
                    PickerResponse.Denied -> println("Denied")
                    is PickerResponse.Picked -> println("Selected ${it.files.first()}")
                }
            }
        }
    ) {
        Text("Pick Image")
    }
}