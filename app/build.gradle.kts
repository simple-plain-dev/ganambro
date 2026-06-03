import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    // kotlin.android sudah built-in di AGP 9.0+
    alias(libs.plugins.kotlin.compose)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use(::load)
    }
}

android {
    namespace = "com.example.ganambro"
    compileSdk = 36 // Android 16 Baklava

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.ganambro"
        minSdk = 23 // Android 6.0 Marshmallow — batas realistis library modern (98.5% coverage)
        targetSdk = 36 // Android 16 Baklava (Play Store minimum ≥ 35)
        versionCode = 1
        versionName = "1.0"

        // Build-time configuration — edit per sekolah
        buildConfigField("String", "SCHOOL_NAME", "\"SMA Negeri 1 Jakarta\"")
        buildConfigField("String", "APP_NAME", "\"Ganambro\"")
        buildConfigField("String", "APP_VERSION", "\"${versionName}\"")
        buildConfigField("String", "TEACHER_PIN", "\"202606\"")
        buildConfigField("String", "URL_PORTAL_UJIAN", "\"https://sites.google.com/\"")
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
}

// AGP 9.0+ built-in Kotlin — JDK mengikuti org.gradle.java.home di gradle.properties


dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.kotlinx.coroutines.core)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
