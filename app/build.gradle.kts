plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.vancoding.tasksapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vancoding.tasksapp"
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
    viewBinding {
        enable = true
    }

    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            val output = this
            val appName = "Tasks"
            val buildType = variant.buildType.name
            val versionCode = variant.versionCode
            val versionName = variant.versionName

            // Customize APK name
            val apkName = "${appName}-${buildType}-v${versionName}-${versionCode}.apk"
            (output as com.android.build.gradle.internal.api.ApkVariantOutputImpl).outputFileName = apkName
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("androidx.fragment:fragment-ktx:1.4.0")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    implementation("io.coil-kt:coil:2.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.github.XuDeveloper:XPopupWindow:1.0.1")

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")

    // To use Kotlin Symbol Processing (KSP)
    kapt("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // ViewModel
    val lifecycle_version = "2.8.2"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    // Image Selector
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.github.lihangleo2:ShadowLayout:3.3.3")
    implementation("io.github.razerdp:BasePopup:3.2.1")
    implementation("io.coil-kt:coil:1.4.0")
}