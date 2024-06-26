
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}
android {
    namespace = "com.learning.cropcare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.learning.cropcare"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding= true
    }

    packagingOptions {
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/DEPENDENCIES")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")


    implementation("com.airbnb.android:lottie:6.3.0")
     // general firebase
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.1.0")

    // view model
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")

    // kotlin couritine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // circular image view
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    // storage
    implementation("com.google.firebase:firebase-storage")

    // pin view
    implementation("io.github.chaosleung:pinview:1.4.4")

    // country code picker
    implementation("com.hbb20:ccp:2.6.0")
    implementation("androidx.browser:browser:1.3.0")

    // firestore
    implementation("com.google.firebase:firebase-firestore")
    // location
    implementation("com.google.android.gms:play-services-location:15.0.1")
    // kommucate
    implementation("io.kommunicate.sdk:kommunicateui:2.9.5")


    val cameraxVersion = "1.4.0-alpha03"
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-view:${cameraxVersion}")
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")


}
