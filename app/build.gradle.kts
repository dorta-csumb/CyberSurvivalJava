plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cybersurvivaljava"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.cybersurvivaljava"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //Enabling the ViewBinding feature that provides a modern way to interact with views
        buildFeatures {
            viewBinding = true
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
            buildFeatures {   //added viewBinding features
                viewBinding = true
            }
        }
    }


    dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview) //added Score Activity recycler view
    implementation("androidx.room:room-runtime:2.8.3")
    annotationProcessor("androidx.room:room-compiler:2.8.3")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}} //this needed a closing curly brace. BUILD SUCCESSFUL and the program is stable