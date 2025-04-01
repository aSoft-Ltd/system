pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("multimodule")
}

fun includeSubs(base: String, path: String = base, vararg subs: String) {
    subs.forEach {
        include(":$base-$it")
        project(":$base-$it").projectDir = File("$path/$it")
    }
}

listOf(
    "kommander", "koncurrent", "kase", "kollections", "kotlinx-interoperable", "cinematic", "status"
).forEach { includeBuild("../$it") }

rootProject.name = "system"

includeSubs("system-file", "file", "core", "pickers", "manager")