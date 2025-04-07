package system

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import koncurrent.later.then
import kotlin.math.round

@Composable
internal fun FilePicker(
    files: FileManager
) {
    Column {
        val picked = remember { mutableStateListOf<LocalFile>() }
        val denied = remember { mutableStateOf(false) }
        Button(
            onClick = {
                files.pickers.documents.openPicker().then {
                    when (it) {
                        is PickerResponse.Cancelled -> {}
                        is PickerResponse.Denied -> denied.value = true
                        is PickerResponse.Picked -> picked += it
                    }
                }
            }
        ) {
            Text("Pick File")
        }

        Column {
            for (file in picked) PickedFile(files.info(file))
        }
    }
}

@Composable
internal fun PickedFile(
    file: FileInfo
) {
    val size = remember(file) {
        val res = file.size().toBestSize()
        res.copy(value = round(res.value * 10) / 10)
    }
    Text(
        "File: ${file.name()}, Size: $size"
    )
}


//