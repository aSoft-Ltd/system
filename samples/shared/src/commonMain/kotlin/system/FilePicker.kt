package system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import system.file.PickerLimit
import system.file.PickingException
import system.file.mime.Application
import system.file.mime.Image
import system.file.picker.response.Cancelled
import system.file.picker.response.Denied
import system.file.picker.response.Failure
import system.file.picker.response.FilePicked
import system.file.picker.response.FilesPicked
import kotlin.math.round

@Composable
internal fun FilesPicker(
    files: FileManager
) {
    val scope = rememberCoroutineScope()
    Column {
        val picked = remember { mutableStateListOf<LocalFile>() }
        val denied = remember { mutableStateOf(false) }
        val errors = remember { mutableStateListOf<PickingException>() }
        Button(
            onClick = {
                scope.launch {
                    when (val response = files.pickers.documents.open(mimes = listOf(Image.JPG, Image.JPEG), limit = PickerLimit(size = 400.KB, count = 2))) {
                        is Cancelled -> {}
                        is Denied -> denied.value = true
                        is Failure -> errors += response
                        is FilesPicked -> picked += response
                    }
                }
            }
        ) {
            Text("Pick Files")
        }

        Button(
            onClick = {
                scope.launch {
                    when (val response = files.pickers.document.open(limit = 10.KB)) {
                        is Cancelled -> {}
                        is Denied -> denied.value = true
                        is Failure -> errors += response.errors
                        is FilePicked -> picked += response.file
                    }
                }
            }
        ) {
            Text("Pick File")
        }

        Column {
            for (file in picked) PickedFile(files.info(file))
        }

        if (errors.isNotEmpty()) Dialog(
            onDismissRequest = {
                errors.clear()
            }
        ) {
            Column(Modifier.fillMaxSize(0.9f)) {
                for ((idx, error) in errors.withIndex()) {
                    Text("${idx + 1}/${errors.size}: ${error.message}")
                }
            }
        }

        if (denied.value) Dialog(
            onDismissRequest = {
                denied.value = false
            }
        ) {
            Column(Modifier.fillMaxSize(0.9f)) {
                Text("Permission denied")
            }
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