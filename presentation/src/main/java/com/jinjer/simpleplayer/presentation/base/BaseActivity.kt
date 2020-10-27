package com.jinjer.simpleplayer.presentation.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jinjer.simpleplayer.presentation.di.activityViewModelsModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.bind
import org.kodein.di.singleton

abstract class BaseActivity: AppCompatActivity(), DIAware {

    // taking an application di object
    private val parentDi: DI by closestDI()

    override val di: DI = DI.lazy {
        extend(parentDi)
        import(activityViewModelsModule)
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(directDI) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}