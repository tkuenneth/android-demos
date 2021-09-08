plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 31
    buildToolsVersion = "31.0.0"

    defaultConfig {
        applicationId = "com.thomaskuenneth.splashscreendemo"
        minSdk = 31
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
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
        useIR = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
}