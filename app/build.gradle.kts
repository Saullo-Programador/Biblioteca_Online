plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.biblioteca"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.biblioteca"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}



dependencies {

    // ==========================================
    // Android Core
    // ==========================================

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)


    // ==========================================
    // Jetpack Compose
    // ==========================================

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)

    implementation(libs.androidx.compose.ui.graphics)

    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.compose.material3)

    debugImplementation(libs.androidx.compose.ui.tooling)

    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // ==========================================
    // Navigation
    // ==========================================

    implementation(libs.androidx.navigation.compose)


    // ==========================================
    // Hilt
    // ==========================================

    implementation(libs.hilt.android)

    implementation(libs.androidx.hilt.navigation.compose)

    ksp(libs.hilt.compiler)


    // ==========================================
    // Room
    // ==========================================

    implementation(libs.androidx.room.runtime)

    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)


    // ==========================================
    // Retrofit
    // ==========================================

    implementation(libs.retrofit)


    // ==========================================
    // OkHttp
    // ==========================================

    implementation(libs.okhttp)

    implementation(libs.okhttp.logging.interceptor)


    // ==========================================
    // Kotlin Serialization
    // ==========================================

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.retrofit.kotlinx.serialization)


    // ==========================================
    // Coil
    // ==========================================

    implementation(libs.coil.compose)


    // ==========================================
    // Coroutines
    // ==========================================

    implementation(libs.kotlinx.coroutines.android)


    // ==========================================
    // Unit Tests
    // ==========================================

    testImplementation(libs.junit)


    // ==========================================
    // Android Tests
    // ==========================================

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )
}