package com.jinjer.simpleplayer.presentation.base

import android.app.Application
import android.content.Context
import com.jinjer.simpleplayer.data.di.dataSourcesModule
import com.jinjer.simpleplayer.domain.utils.DIConstants.tagClientController
import com.jinjer.simpleplayer.domain.utils.DIConstants.tagPlayerController
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.controller.main.IClientController
import com.jinjer.simpleplayer.presentation.controller.main.IPlayerController
import com.jinjer.simpleplayer.presentation.controller.main.MainController
import com.jinjer.simpleplayer.presentation.di.useCasesModule
import com.jinjer.simpleplayer.presentation.utils.notifications.INotifyManager
import com.jinjer.simpleplayer.presentation.utils.notifications.NotifyManager
import org.kodein.di.*

class Application: Application(), DIAware {
    override val di = DI.lazy {
        import(dataSourcesModule)
        import(useCasesModule)

        bind<Context>() with singleton { this@Application.applicationContext }
        bind<INotifyManager>() with singleton { NotifyManager(instance()) }

        bind<IClientController>(tagClientController) with singleton { MainController(instance()) }
        bind<IPlayerController>(tagPlayerController) with singleton { instance(tagClientController) as IPlayerController }

        constant("appName") with getString(R.string.app_name)
    }
}