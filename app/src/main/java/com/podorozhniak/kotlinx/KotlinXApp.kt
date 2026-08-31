package com.podorozhniak.kotlinx

import android.app.Application
import com.facebook.stetho.Stetho
import com.podorozhniak.kotlinx.practice.data.remote.service.MessagesService
import com.podorozhniak.kotlinx.practice.di.dataSourceModule
import com.podorozhniak.kotlinx.practice.di.repositoryModule
import com.podorozhniak.kotlinx.practice.di.retrofitModule
import com.podorozhniak.kotlinx.practice.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KotlinXApp : Application() {

    var messageApi: MessagesService? = null

    override fun onCreate() {
        super.onCreate()
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )
        startKoin {
            androidContext(this@KotlinXApp)
            modules(listOf(retrofitModule, dataSourceModule, repositoryModule, viewModelsModule))
        }
    }
}