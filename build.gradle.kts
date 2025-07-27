// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

buildscript {
    ext {
        compose_ui_version = "1.7.2"
    }
    repositories {
        google()
        mavenCentral()
    }
}
