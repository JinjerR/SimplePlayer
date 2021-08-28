package com.jinjer.simpleplayer.dependencies

object Deps {
    val material by lazy { "com.google.android.material:material:${Versions.material}" }
    val buildGradleTools by lazy { "com.android.tools.build:gradle:${Versions.buildGradleTools}" }

    object Kotlin {
        val kotlin by lazy { "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}" }
        val gradlePlugin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" }
    }

    object AndroidX {
        val coreKtx by lazy { "androidx.core:core-ktx:${Versions.AndroidX.coreKtx}" }
        val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}" }
    }

    object Testing {
        val jUnit by lazy { "junit:junit:${Versions.Testing.jUnit}" }
        val androidXjUnitExt by lazy { "androidx.test.ext:junit:${Versions.Testing.androidXJunit}" }
        val androidXEspresso by lazy { "androidx.test.espresso:espresso-core:${Versions.Testing.androidXEspresso}" }
    }
}