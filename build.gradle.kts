// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        // Other repositories...
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
        classpath ("com.android.tools.build:gradle:8.8.2")
        classpath ("com.google.gms:google-services:4.3.10")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
