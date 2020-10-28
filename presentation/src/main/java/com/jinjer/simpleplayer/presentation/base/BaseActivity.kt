package com.jinjer.simpleplayer.presentation.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jinjer.simpleplayer.presentation.di.viewModelsModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

abstract class BaseActivity: AppCompatActivity(), DIAware {

    // taking an application di object
    private val parentDi: DI by closestDI()

    override val di: DI = DI.lazy {
        extend(parentDi)
        import(viewModelsModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}