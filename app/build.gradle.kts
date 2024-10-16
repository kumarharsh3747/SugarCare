plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.sugarfree"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sugarfree"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Material and Icons
    implementation("androidx.compose.material:material-icons-extended:1.5.0")

    // Core AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM (Bill of Materials) for consistent versions
    implementation(platform(libs.androidx.compose.bom))

    // Jetpack Compose Libraries
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation Components for Compose
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // CameraX Dependencies
    implementation ("androidx.camera:camera-camera2:1.2.3")
    implementation ("androidx.camera:camera-lifecycle:1.2.3")
    implementation ("androidx.camera:camera-view:1.3.0")
    implementation ("com.google.mlkit:text-recognition:16.0.0")

    // ML Kit for Text Recognition
    implementation("com.google.mlkit:text-recognition:16.0.0")

    // Accompanist for Permission Handling
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Generative AI Library
    implementation(libs.generativeai)

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging Tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.compose.ui:ui:1.5.2")
    implementation ("androidx.compose.ui:ui-tooling:1.5.2")
    implementation ("androidx.compose.ui:ui-viewbinding:1.5.2")

    implementation ("androidx.compose.ui:ui:1.5.2")
    implementation ("androidx.compose.material3:material3:1.2.0") // Optional, for Material3 usage
    implementation ("androidx.compose.ui:ui-viewbinding:1.5.2")  // Required for AndroidView
    implementation ("androidx.activity:activity-compose:1.7.2")

    implementation ("androidx.camera:camera-core:1.3.0")
    implementation ("androidx.camera:camera-camera2:1.3.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-view:1.3.0")
    implementation ("com.google.accompanist:accompanist-permissions:0.33.0-alpha")

    implementation ("androidx.compose.material3:material3:1.2.0") // Replace with the latest version
    implementation ("androidx.compose.ui:ui:1.3.0" )// Replace with the latest version
    implementation ("androidx.navigation:navigation-compose:2.5.0" )//

    implementation ("io.coil-kt:coil-compose:2.3.0")

    implementation ("androidx.navigation:navigation-compose:2.7.1")
    implementation ("io.coil-kt:coil-compose:2.2.2") // For image loading

    implementation ("androidx.compose.ui:ui:1.5.0"    )       // Replace with latest version
    implementation ("androidx.compose.material3:material3:1.1.0")
    implementation ("androidx.navigation:navigation-compose:2.7.0")
    implementation ("io.coil-kt:coil-compose:2.3.0")
}

