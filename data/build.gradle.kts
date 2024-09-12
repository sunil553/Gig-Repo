plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.llyod.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation (libs.androidx.retrofit2.retrofit)
    implementation (libs.androidx.retrofit2.gson.converter)
    implementation (libs.converter.scalars)
    implementation (libs.logging.interceptor.v480)
    implementation (libs.okhttp.v4120)


    //Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Testing
    testImplementation (libs.junit)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.inline)
    testImplementation (libs.turbine)

    testImplementation(libs.mockito.kotlin)
    implementation(libs.androidx.annotation)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}