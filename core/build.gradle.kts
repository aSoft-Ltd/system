import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A kotlin multiplatform abstraction for reading files as blobs"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library() }
    if (Targeting.WASM) wasmJs { library() }
//    if (Targeting.WASM) wasmWasi { library() }
    val osxTargets = if (Targeting.OSX) (iosTargets() + macOsTargets()) else listOf()
//    val ndkTargets = if (Targeting.NDK) ndkTargets() else listOf()
    val linuxTargets = if (Targeting.LINUX) linuxTargets() else listOf()
//    val mingwTargets = if (Targeting.MINGW) mingwTargets() else listOf()
    val nativeTargets = osxTargets + linuxTargets

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.koncurrent.later.coroutines)
                api(ktor.client.core)
                api(libs.kase.core)
                api(libs.kotlinx.exports)
                api(libs.status.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.koncurrent.later.test)
                implementation(kotlinx.serialization.json)
                implementation(libs.kommander.coroutines)
            }
        }

        val wasmMain by creating {
            dependsOn(commonMain)
        }

        if (Targeting.WASM) {
            val wasmJsMain by getting {
                dependsOn(wasmMain)
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        nativeTargets.forEach {
            val main by it.compilations.getting {}
            main.defaultSourceSet {
                dependsOn(nativeMain)
            }
        }
    }
}

rootProject.the<NodeJsRootExtension>().apply {
    version = npm.versions.node.version.get()
    downloadBaseUrl = npm.versions.node.url.get()
}

rootProject.tasks.withType<KotlinNpmInstallTask>().configureEach {
    args.add("--ignore-engines")
}

tasks.named("wasmJsTestTestDevelopmentExecutableCompileSync").configure {
    mustRunAfter(tasks.named("jsBrowserTest"))
    mustRunAfter(tasks.named("jsNodeTest"))
}