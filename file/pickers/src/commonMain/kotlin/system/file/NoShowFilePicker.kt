package system.file

import koncurrent.Later
import system.PickerResponse
import system.file.mime.Mime

class NoShowFilePicker : FilePicker {
    override fun openPicker(
        mimes: List<Mime>,
        multiple: Boolean
    ): Later<PickerResponse> = Later(PickerResponse.Cancelled)
}