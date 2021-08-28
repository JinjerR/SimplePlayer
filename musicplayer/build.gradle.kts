import com.jinjer.simpleplayer.dependencies.Deps

plugins {
    id("com.android.library")
    kotlin("android")
    id("com.jinjer.simpleplayer.config")
}

dependencies {
    implementation(project(":musicplayer:musicplayer-presentation"))

    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.material)
    implementation(Deps.Testing.jUnit)
    implementation(Deps.Testing.androidXjUnitExt)
    implementation(Deps.Testing.androidXEspresso)
}