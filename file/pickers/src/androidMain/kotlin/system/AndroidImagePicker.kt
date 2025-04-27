package system

//class AndroidImagePicker(private val activity: ComponentActivity) : FilePicker {
//    private var scope: CoroutineScope? = null
//    private val permission by lazy { AndroidPickerPermissionManager(activity, scope) }
//    private var launcher: ActivityResultLauncher<PickVisualMediaRequest>? = null
//    private val results by lazy { Channel<List<Uri>>() }
//
//    fun register() {
//        if (launcher != null) return
//        scope = CoroutineScope(SupervisorJob())
//        launcher = activity.registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
//            scope?.launch { results.send(uris) }
//        }
//        permission.register()
//    }
//
//    private fun launchPicker(
//        mimes: List<Mime>,
//        multiple: Boolean
//    ) = Later { resolve, reject ->
//        val s = scope ?: return@Later reject(
//            IllegalStateException("AndroidFileChooser has not been registered")
//        )
//        val l = launcher ?: return@Later reject(
//            IllegalStateException("AndroidFileChooser has not been registered")
//        )
//
//        val request = PickVisualMediaRequest.Builder()
//            .setMediaType(mimes.toMediaType())
//            .build()
//        l.launch(request)
//
//        s.launch {
//            val files = results.receive().mapNotNull { it.path }.map { LocalFileImpl(it) }
//            resolve(files.toResponse(multiple))
//        }
//    }
//
//    private fun List<Mime>.toMediaType() = when {
//        isEmpty() -> ImageAndVideo
//        all { it is Image } -> ImageOnly
//        all { it is Video } -> VideoOnly
//        else -> ImageAndVideo
//    }
//
//    override fun openPicker(
//        mimes: List<Mime>,
//        multiple: Boolean
//    ): Later<PickerResponseOld> {
//        if (mimes.isEmpty()) return openPicker(listOf(All), multiple)
//        if (permission.check(mimes) == Permission.Granted) return launchPicker(mimes, multiple)
//        return permission.request(mimes).andThen { permit ->
//            when (permit) {
//                Permission.Granted -> launchPicker(mimes, multiple)
//                else -> Later(PickerResponseOld.Denied)
//            }
//        }
//    }
//
//    fun unregister() {
//        permission.unregister()
//        scope?.cancel()
//        scope = null
//    }
//}