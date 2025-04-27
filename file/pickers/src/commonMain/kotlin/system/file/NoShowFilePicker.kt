package system.file

import koncurrent.Later
import system.PickerResponseOld
import system.file.mime.Mime

class NoShowFilePicker : FilePicker {
    override fun openPicker(
        mimes: List<Mime>,
        multiple: Boolean
    ): Later<PickerResponseOld> = Later(PickerResponseOld.Cancelled)
}