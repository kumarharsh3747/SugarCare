plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.sugarfree"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sugarCare"
        minSdk = 26
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
    implementation ("com.opencsv:opencsv:5.5.2")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.1")

    // ML Kit for Text Recognition
    implementation("com.google.mlkit:text-recognition:16.0.0")

    // Accompanist for Permission Handling
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Generative AI Library
    implementation(libs.generativeai)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.engage.core)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.play.services.mlkit.barcode.scanning)
    implementation(libs.androidx.tools.core)

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

    implementation ("com.google.mlkit:text-recognition:16.0.0")  //Google's ML Kit Text Recognition API.

    implementation ("androidx.navigation:navigation-compose:2.5.3")
    implementation ("com.google.accompanist:accompanist-permissions:0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:1.0.0")
    implementation("com.google.firebase:firebase-auth:21.1.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation("com.google.firebase:firebase-database:20.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation (platform("com.google.firebase:firebase-bom:28.0.1"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("androidx.core:core-ktx:1.7.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation(platform("com.google.firebase:firebase-bom:33.5.0"))
    implementation("com.google.firebase:firebase-analytics")


    dependencies {
        implementation ("com.squareup.retrofit2:retrofit:2.9.0")
        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    }


    implementation("com.google.ai.client.generativeai:generativeai:0.1.2")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:1.0.0")
    implementation("com.google.firebase:firebase-auth:21.1.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation("com.google.firebase:firebase-database:20.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation (platform("com.google.firebase:firebase-bom:28.0.1"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-database-ktx")
    implementation ("androidx.core:core-ktx:1.7.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")


    dependencies {
        implementation ("com.squareup.retrofit2:retrofit:2.9.0")
        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    }


    implementation("com.google.ai.client.generativeai:generativeai:0.1.2")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("androidx.compose.foundation:foundation-layout:1.0.0")

    implementation ("androidx.compose.ui:ui:1.5.0")
    implementation ("androidx.compose.material3:material3:1.2.0") // Use Material 3
    implementation ("androidx.navigation:navigation-compose:2.7.0")
    implementation ("io.coil-kt:coil-compose:2.3.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0") // For ViewModel support
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc01")

    implementation ("androidx.compose.material3:material3:1.1.0")  // or the latest version
    implementation ("androidx.compose.ui:ui:1.5.0")  // or the latest version
    implementation ("androidx.compose.ui:ui-text:1.5.0")  // or the latest version

    implementation ("com.google.firebase:firebase-firestore:24.5.0")
    implementation ("androidx.navigation:navigation-compose:2.7.0")

    implementation ("androidx.compose.runtime:runtime-livedata:<compose_version>")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:<coroutines_version>")
    // Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")
    // Animation support
    implementation ("androidx.compose.animation:animation-core:1.6.0")
    implementation ("androidx.compose.animation:animation:1.6.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("androidx.work:work-runtime-ktx:2.8.1")



}