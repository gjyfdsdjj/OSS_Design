plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.checkmate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.checkmate"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.7.0") // ComponentActivity와 setContent
    implementation("androidx.compose.ui:ui:1.4.0") // Compose UI 기본 라이브러리
    implementation("androidx.compose.material3:material3:1.0.1") // Material3 UI 구성 요소
    implementation("androidx.compose.foundation:foundation:1.4.0") // Compose Foundation (LazyColumn, Row 등)
    implementation("androidx.compose.runtime:runtime:1.4.0") // Compose Runtime
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0") // UI 미리보기를 위한 라이브러리
    implementation("androidx.compose.ui:ui-tooling:1.4.0")// Material3 UI 구성 요소
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}