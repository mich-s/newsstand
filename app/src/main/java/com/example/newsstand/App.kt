package com.example.newsstand

import android.app.Application
import com.example.newsstand.di.AppComponent
import com.example.newsstand.di.DaggerAppComponent
import com.facebook.stetho.Stetho
import timber.log.Timber

class App: Application(){

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(Timber.DebugTree())
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

}