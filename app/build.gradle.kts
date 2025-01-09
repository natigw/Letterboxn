plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.letterboxn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.letterboxn"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
}

dependencies {

    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //okHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //Glide
    implementation (libs.glide)

    //coil
    implementation("io.coil-kt:coil:2.7.0")

    //Firebase
    implementation ("com.google.firebase:firebase-auth:23.0.0")
    //Firestore
    implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")

//    //google auth
//    implementation("com.google.android.gms:play-services-auth:21.2.0")

    //navigation drawer
    implementation ("com.google.android.material:material:1.12.0")

    //hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-android-compiler:2.46.1")

    //biometric
    implementation("androidx.biometric:biometric:1.1.0")

    //google sing in
    implementation("com.github.TutorialsAndroid:GButton:v1.0.19")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    //paging
    implementation("androidx.paging:paging-runtime:3.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    //customformatter
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    //snackbar
    implementation("com.google.android.material:material:1.12.0")

    //splashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    //shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //room database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}