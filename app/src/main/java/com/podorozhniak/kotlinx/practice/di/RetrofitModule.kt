package com.podorozhniak.kotlinx.practice.di

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.podorozhniak.kotlinx.practice.data.remote.repository.MessagesRepoImpl
import com.podorozhniak.kotlinx.practice.data.remote.service.MessagesDataSource
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.ResultAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT_SEC = 30L
private const val BASE_URL = "https://rawgit.com/startandroid/data/master/messages/"

val retrofitModule = module {
    single { provideGson() }
    single { provideCallFactory() }
    single { provideGsonConverterFactory(get()) }
    single { provideHttpCache(androidApplication()) }
    single { provideOkhttpClient(get()) }
    single { provideRetrofit(get(), get()) }
}

fun provideGson(): Gson = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .setLenient()
    .serializeNulls()
    .create()

fun provideCallFactory(): CallAdapter.Factory = CoroutineCallAdapterFactory()
fun provideGsonConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

fun provideHttpCache(application: Application): Cache {
    val cacheSize = 10L * 1024 * 1024
    return Cache(application.cacheDir, cacheSize)
}

fun provideOkhttpClient(cache: Cache): OkHttpClient {
    return OkHttpClient.Builder().apply {
        cache(cache)
        connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)

        addInterceptor { chain ->
            val request: Request = chain.request()

            val change: Request.Builder = request.newBuilder().apply {
                header("Content-Type", "application/json")
            }

            chain.proceed(change.build())
        }

        //if (BuildConfig.DEBUG) {
        // Should be last in the chain!!!
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(httpLoggingInterceptor)
        //}
    }.build()
}

fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(ResultAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()






