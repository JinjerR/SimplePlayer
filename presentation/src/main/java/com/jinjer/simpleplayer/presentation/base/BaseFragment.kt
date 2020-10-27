package com.jinjer.simpleplayer.presentation.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jinjer.simpleplayer.presentation.di.fragmentViewModelsModule
import org.kodein.di.*
import org.kodein.di.android.di

abstract class BaseFragment: Fragment(), DIAware {

    // taking an application di object
    private val parentDi: DI by di { requireContext().applicationContext }

    override val di: DI = DI.lazy {
        extend(parentDi)
        import(fragmentViewModelsModule)
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(directDI) }
    }
}