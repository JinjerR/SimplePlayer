package com.jinjer.simpleplayer.config

import org.gradle.api.Plugin
import org.gradle.api.Project

open class SimplePlayerConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val configOptions: ConfigOptions = project.extensions.create(
            "configOptions", ConfigOptions::class.java
        )
        project.configureAndroid(configOptions)
    }
}

