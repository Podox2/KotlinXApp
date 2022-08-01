package com.podorozhniak.kotlinx

import android.app.Application
import com.facebook.stetho.Stetho
import com.podorozhniak.kotlinx.practice.data.remote.service.MessagesService
import com.podorozhniak.kotlinx.practice.di.dataSourceModule
import com.podorozhniak.kotlinx.practice.di.repositoryModule
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.ResultAdapterFactory
import com.podorozhniak.kotlinx.practice.di.retrofitModule
import com.podorozhniak.kotlinx.practice.di.viewModelsModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        provideMessageApi()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(retrofitModule, dataSourceModule, repositoryModule, viewModelsModule))
        }
    }

    /*retrofit надстройка над okhttp, дозволяє додавати конвертери і т.п.
    okhttp бібліотека, яка робить за нас кучу всяких двіжух для роботи з мережею
    okhttp logging interceptor надає нам логи по запитам в мережу*/
    fun provideMessageApi(): MessagesService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawgit.com/startandroid/data/master/messages/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(ResultAdapterFactory())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        messageApi = retrofit.create(MessagesService::class.java)
        return retrofit.create(MessagesService::class.java)
    }
}