import com.jinjer.simpleplayer.dependencies.Deps

plugins {
    id("com.android.library")
    kotlin("android")
    id("com.jinjer.simpleplayer.config")
    id("kotlin-android")
}

dependencies {
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.material)
    implementation(Deps.Testing.jUnit)
    implementation(Deps.Testing.androidXjUnitExt)
    implementation(Deps.Testing.androidXEspresso)
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
}