import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    jvm {
        compilations.all {
            compileTaskProvider {
                compilerOptions.jvmTarget = JvmTarget.JVM_17
            }
        }
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.samplesShared)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "system.samples.MainKt"

        buildTypes.release.proguard {
            version = "7.4.0"
            isEnabled = true
            optimize = true
            obfuscate = true
            joinOutputJars = true
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb, TargetFormat.Exe, TargetFormat.Msi)
            packageName = "System"
            vendor = "aSoft"
            linux {
                iconFile = file("src/commonMain/resources/logo/monogram_blue_gradient.svg")
                shortcut = true
            }

            macOS {
                bundleID = "tz.co.asoft.academia"
                iconFile = file("src/commonMain/resources/logo/monogram_blue_gradient.icns")

                signing {
                    sign.set(true)
                    identity.set("Msangya Anderson")
                    // keychain.set("/path/to/keychain")
                }

                notarization {
                    val path = gradleLocalProperties(rootDir)
                    appleID.set(path.getProperty("NOTARIZATION_APPLE_ID"))
                    password.set(path.getProperty("NOTARIZATION_PASSWORD"))
                    teamID.set(path.getProperty("NOTARIZATION_TEAM_ID"))
                }
            }

            windows {
                iconFile = file("src/commonMain/resources/logo/monogram_blue_gradient.svg")
                shortcut = true
            }
        }
    }
}
