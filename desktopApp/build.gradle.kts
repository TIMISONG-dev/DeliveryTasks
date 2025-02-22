import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    alias(libs.plugins.compose.compiler)
    id("app.cash.sqldelight") version "2.0.2"
}

group = "timisongdev.deliverytasks"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.animation)
    implementation(libs.skiko.awt.runtime.windows.x64)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.json.serialization)
    implementation(libs.ktor.client.java)
    implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
}

//sqldelight {
//    databases {
//        create("WarehouseDB") {
//            packageName.set("com.deliverytasks")
//            srcDirs.setFrom("src/main/res/files")
//        }
//    }
//}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "DeliveryTasks"
            packageVersion = "1.0.0"
            description = "App for Couriers work and Couriers Support"
            vendor = "TIMISONG-dev"

            windows {
                menuGroup = "DeliveryTasks"

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
            val main by getting {
                dependencies {
                    implementation(libs.kotlinx.serialization.json)
                }
            }
        }
    }
}
