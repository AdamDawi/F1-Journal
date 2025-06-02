import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildConfig)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val baseServerUrl = localProperties.getProperty("BASE_URL") ?: ""

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            //networking
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)

            //navigation
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

            //di
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            //image loading
            implementation(libs.coil.mp)

            //fix logging error
            implementation("org.slf4j:slf4j-api:1.7.32")  // SLF4J API
            implementation("ch.qos.logback:logback-classic:1.2.6")  // Logback -  SLF4J

            // converter XML -> JSON
            implementation("org.json:json:20230227")

            //chart library
            implementation(libs.vico.multiplatform)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

buildConfig {
    packageName("org.adamdawi.f1journal")
    buildConfigField("String", "BASE_URL", "\"$baseServerUrl\"")
}

compose.desktop {

    application {
        mainClass = "org.adamdawi.f1journal.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.adamdawi.f1journal"
            packageVersion = "1.0.0"
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/f1-logo.ico"))
            }
        }
    }
}
