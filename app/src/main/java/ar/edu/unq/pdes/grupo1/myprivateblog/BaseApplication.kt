package ar.edu.unq.pdes.grupo1.myprivateblog

import android.app.Application
import ar.edu.unq.pdes.grupo1.myprivateblog.BuildConfig
import ar.edu.unq.pdes.grupo1.myprivateblog.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class BaseApplication : Application(), HasAndroidInjector {

    @Inject
    @JvmField
    var dispatchingAndroidInjector: DispatchingAndroidInjector<Any?>? = null

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.factory().create(applicationContext)
            .inject(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun androidInjector(): AndroidInjector<Any?>? {
        return dispatchingAndroidInjector
    }

}