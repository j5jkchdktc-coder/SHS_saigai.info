plugins {
    id("kotlin-kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

}

android {
    namespace = "jp.shsit.shsinfo2025"
    compileSdk = 36

    defaultConfig {
        applicationId = "shsit.jp.shsinfo2021"
        minSdk = 24
        targetSdk = 36
        versionCode = 52
        versionName = "1.9.2"

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
    buildFeatures {
        dataBinding = true
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //osmdroidで地図を表示するために
    implementation("org.osmdroid:osmdroid-android:6.1.13")
    //位置情報
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    // room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // viewmodel
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.8.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    // Kotlin標準ライブラリを追加
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.21")
    //pdfview
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.1") //barteksc 3.2.0-beta.1
    //ARCore関係
    implementation("com.google.ar:core:1.44.0")
    implementation("com.google.ar.sceneform.ux:sceneform-ux:1.17.1")
    implementation("com.google.ar.sceneform:core:1.17.1")
    //地図の倍率を下げたときにごちゃごちゃになってしまうので、ある程度の範囲内のマーカーをグループ化してまとめる方法
    //implementation("com.github.MKergall:osmbonuspack:6.6.0")
    //floatingActionButtonを追加
    implementation("com.google.android.material:material:1.12.0")
    //kotlinでプレファレンスAPI29レベル以降で使用できないので追加
    implementation("androidx.preference:preference:1.2.1")
    //liveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

}