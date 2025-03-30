package epsilon

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

actual fun SystemFileReader(): FileReader = JvmFileManager(scope = CoroutineScope(SupervisorJob()), http = HttpClient())