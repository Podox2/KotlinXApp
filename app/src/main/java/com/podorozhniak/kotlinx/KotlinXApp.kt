package com.podorozhniak.kotlinx

import android.app.Application
import com.podorozhniak.kotlinx.theory.coroutines.MessageApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KotlinXApp : Application() {

    lateinit var messageApi: MessageApi

    override fun onCreate() {
        super.onCreate()
        provideMessageApi()
        startKoin {
            androidContext(applicationContext)
        }
    }

    /*retrofit надстройка над okhttp, дозволяє додавати конвертери і т.п.
    okhttp бібліотека, яка робить за нас кучу всяких двіжух для роботи з мережею
    okhttp logging interceptor надає нам логи по запитам в мережу*/
    private fun provideMessageApi() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawgit.com/startandroid/data/master/messages/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        messageApi = retrofit.create(MessageApi::class.java)
    }
}