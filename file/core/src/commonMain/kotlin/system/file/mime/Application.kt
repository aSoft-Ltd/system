package system.file.mime

interface Application : Mime {
    override val name: String
    override val text: String

    companion object : Application {
        override val name: String = "Application"
        override val text: String = "application/*"
    }

    data object JSON : Application {
        override val name: String get() = "JSON"
        override val text: String get() = "application/json"
    }

    data object XML : Application {
        override val name: String get() = "XML"
        override val text: String get() = "application/xml"
    }

    data object ZIP : Application {
        override val name: String get() = "ZIP"
        override val text: String get() = "application/zip"
    }

    data object GZIP : Application {
        override val name: String get() = "GZIP"
        override val text: String get() = "application/gzip"
    }
}