plugins {
    alias(libs.plugins.android.application) apply false
    // kotlin.android sudah built-in di AGP 9.0+
    alias(libs.plugins.kotlin.compose) apply false
}
