plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
    //Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    //Hilt
    implementation (libs.hilt.core)


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
