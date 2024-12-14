plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.wemahostels"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.wemahostels"
        minSdk = 24
        targetSdk = 34
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.play.services.maps)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation (libs.ui ) // Replace with the latest Compose version
    implementation (libs.material3  )// Replace with the latest version
    implementation (libs.ui.tooling ) // Replace with the latest version
    implementation(libs.coil.compose)
    implementation (libs.maps.compose)
    implementation (libs.play.services.maps.v1810)
    implementation( libs.androidx.ui.vlatestversion)
    implementation (libs.androidx.material3.vlatestversion)
    implementation (libs.androidx.runtime)
    implementation (libs.androidx.animation)
    implementation( libs.accompanist.pager )// CAROUSEL
    implementation (libs.maps.compose) // Use the latest version
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp.v491)
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v250)
    implementation (libs.androidx.activity.compose.v160)
//PESAPAL API
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v2xx)
    implementation( libs.androidx.lifecycle.livedata.ktx)
    implementation( libs.androidx.runtime.livedata.v1xx)
    implementation (libs.androidx.ui.v1xx)
    implementation (libs.androidx.material3.v1xx)

        // Coroutines dependencies
        implementation (libs.kotlinx.coroutines.android.v150)
        implementation( libs.kotlinx.coroutines.core)






}







