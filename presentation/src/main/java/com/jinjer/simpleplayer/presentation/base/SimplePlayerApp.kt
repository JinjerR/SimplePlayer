package com.jinjer.simpleplayer.presentation.base

import android.app.Application
import android.content.Context
import com.jinjer.simpleplayer.data.di.dataSourcesModule
import com.jinjer.simpleplayer.domain.utils.DIConstants
import com.jinjer.simpleplayer.domain.utils.DIConstants.tagApplication
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.di.mappersModule
import com.jinjer.simpleplayer.presentation.di.useCasesModule
import com.jinjer.simpleplayer.presentation.utils.notifications.INotifyManager
import com.jinjer.simpleplayer.presentation.utils.notifications.NotifyManager
import org.kodein.di.*

class SimplePlayerApp: Application(), DIAware {
    override val di = DI.lazy {
        import(dataSourcesModule)
        import(useCasesModule)
        import(mappersModule)

        bind<Context>() with singleton { this@SimplePlayerApp.applicationContext }
        bind<INotifyManager>() with singleton { NotifyManager(instance()) }
        bind<SimplePlayerApp>(tagApplication) with singleton { this@SimplePlayerApp }

        constant("appName") with getString(R.string.app_name)
    }
}