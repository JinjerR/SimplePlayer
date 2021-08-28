import com.jinjer.simpleplayer.dependencies.Deps

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.jinjer.simpleplayer.config")
}

configOptions {
    applicationId = "com.jinjer.simpleplayer"
}

dependencies {
    implementation(project(":musicplayer"))

    implementation(Deps.Kotlin.kotlin)
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.material)

    testImplementation(Deps.Testing.jUnit)
    androidTestImplementation(Deps.Testing.androidXjUnitExt)
    androidTestImplementation(Deps.Testing.androidXEspresso)
}