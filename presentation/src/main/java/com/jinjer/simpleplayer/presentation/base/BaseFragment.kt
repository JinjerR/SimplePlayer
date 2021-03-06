package com.jinjer.simpleplayer.presentation.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jinjer.simpleplayer.presentation.controller.main.IMainController
import com.jinjer.simpleplayer.presentation.controller.main.MainViewModel
import com.jinjer.simpleplayer.presentation.di.viewModelsModule
import com.jinjer.simpleplayer.presentation.utils.extensions.getFactoryInstance
import org.kodein.di.*
import org.kodein.di.android.di

abstract class BaseFragment: Fragment(), DIAware {

    protected val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity(), getFactoryInstance()).get(MainViewModel::class.java)
    }

    protected var mainController: IMainController? = null

    // taking an application di object
    private val parentDi: DI by di { requireContext().applicationContext }

    override val di: DI = DI.lazy {
        extend(parentDi)
        import(viewModelsModule)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainController = context as? IMainController
    }
}