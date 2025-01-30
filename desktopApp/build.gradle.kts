import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    alias(libs.plugins.compose.compiler)
}

group = "timisongdev.mytasks"
version = "1.0-SNAPSHOT"

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.animation:animation:1.7.3")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.8.19")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "myTasks"
            packageVersion = "1.0.0"
            description = "App for Couriers work and Couriers Admins"
            vendor = "TIMISONG-dev"

            windows {
                menuGroup = "myTasks Group"

                packageVersion = "1.0.0"
                shortcut = true
                menu = true
            }
        }
    }

    kotlin {

        sourceSets {
            main {
                resources.srcDirs("src/main/res/drawable")
            }
        }
    }
}
