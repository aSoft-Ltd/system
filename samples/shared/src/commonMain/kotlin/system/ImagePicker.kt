package system

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.Dialog
import koncurrent.later.catch
import koncurrent.later.then
import kotlinx.coroutines.launch
import system.file.PickerLimit
import system.file.PickingException
import system.file.mime.Image
import system.file.picker.response.Cancelled
import system.file.picker.response.Denied
import system.file.picker.response.Failure
import system.file.picker.response.FilePicked
import system.file.picker.response.FilesPicked

@Composable
internal fun ImagePicker(
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
                    when (val response = files.pickers.media.open(mimes = listOf(Image))) {
                        is Cancelled -> {}
                        is Denied -> denied.value = true
                        is Failure -> errors += response.errors
                        is FilePicked -> picked += response.file
                    }
                }
            }
        ) {
            Text("Pick Image")
        }

        Button(
            onClick = {
                scope.launch {
                    when (val response = files.pickers.medias.open(mimes = listOf(Image), limit = PickerLimit(count = 3, size = 20.KB))) {
                        is Cancelled -> {}
                        is Denied -> denied.value = true
                        is Failure -> errors += response
                        is FilesPicked -> picked += response
                    }
                }
            }
        ) {
            Text("Pick Images")
        }

        Column {
            for (file in picked) PickedImage(files, file)
        }

        if (errors.isNotEmpty()) Dialog(
            onDismissRequest = { errors.clear() }
        ) {
            Column {
                for (error in errors) Text(error.message)
            }
        }
    }
}

@Composable
internal fun PickedImage(
    files: FileManager,
    file: LocalFile
) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    val info = remember { files.info(file) }
    val name = info.name()
    println("Showing $name")
    LaunchedEffect(file) {
        println("Reading $name")
        files.read(file).then {
            println("Read $name")
            image = it.toImageBitmap()
            println("Assigned $image to $name")
        }.catch {
            println("Failed to read $name: $it")
        }
    }
    when (val i = image) {
        null -> Text("Loading $name...")
        else -> Image(i, contentDescription = info.name())
    }
}