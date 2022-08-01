package com.podorozhniak.kotlinx.practice.di

import android.app.Application
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.ResultAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT_SEC = 30L
private const val BASE_URL = "https://rawgit.com/startandroid/data/master/messages/"
private const val CACHE_SIZE = 5L * 1024 * 1024 // 5 Mb
private const val PRAGMA_HEADER = "pragma"
private const val CACHE_CONTROL_HEADER = "cache-control"

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
// надає файл для зберігання кеша
fun provideHttpCache(application: Application): Cache = Cache(application.cacheDir, CACHE_SIZE)

fun provideOkhttpClient(cache: Cache): OkHttpClient {
    return OkHttpClient.Builder().apply {
        cache(cache)
        connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)
        // якщо StethoInterceptor буде доданий після інтерцепторів для кешування, то в еджі будуть відображатись не оновлені хедери.
        // логкат студії показує оновлені хедери. тому певно просто проксі не правильно показує.
        // чарльз в будь-якому випадку не показує оновлені хедери
        addNetworkInterceptor(StethoInterceptor())
        //можна додавати інтерцептори як анонімний клас
        /*addNetworkInterceptor { chain ->
            chain.proceed(chain.request()).newBuilder()
                .removeHeader(PRAGMA_HEADER)
                .removeHeader(CACHE_CONTROL_HEADER)
                .header(CACHE_CONTROL_HEADER, "max-age=12")
                .addHeader("response-header", "test-value")
                .build()
        }*/
        // можна оновлювати хедери і для реквестів, і для респонсів
        addInterceptor(headerInterceptorObject) // Interceptor спрацьовує коли немає мережі
        addNetworkInterceptor(networkCachingInterceptorLambda) // NetworkInterceptor спрацьовує тільки коли є мережа
        addInterceptor(provideOfflineCachingInterceptorFunction())

        //if (BuildConfig.DEBUG) {
        // must be the last interceptor to catch and log modified requests
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        addInterceptor(httpLoggingInterceptor)
        //}
    }.build()
}

// реалізація через анонімний клас
// додає хедер в реквест
val headerInterceptorObject = object : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val change: Request.Builder = request.newBuilder().apply {
            header("Another-Header", "another-value")
        }
        return chain.proceed(change.build())
    }
}

// тільки коли є мережа
// реалізація через анонімний клас, який конвертований в лямбду
// додає хедер в респонс
val networkCachingInterceptorLambda = Interceptor { chain ->
    Log.d("Cache", "online cached")
    val response: Response = chain.proceed(chain.request())

    // кешує реквест на 5 секунд. тобто якщо повторити той самий реквест за 5 секунд,
    // реквест не буде надісланий на сервер, респонс дістанеться з кеша
    val cacheControl: CacheControl = CacheControl.Builder()
        .maxAge(5, TimeUnit.SECONDS)
        .build()

    response.newBuilder()
        .removeHeader(PRAGMA_HEADER)
        .removeHeader(CACHE_CONTROL_HEADER)
        .header(CACHE_CONTROL_HEADER, cacheControl.toString())
        .addHeader("Response-header-2", "new-test-value")
        .build()
}

// для оффлайн режима
// лямбда, але її повертає метод
private fun provideOfflineCachingInterceptorFunction(): Interceptor =
    Interceptor { chain ->
        Log.d("Cache", "offline cached")
        var request: Request = chain.request()

        //різні варіанти для value в хедері
        val maxStale = 60 * 60 * 24 * 3 // Offline cache available for 3 days
        val cacheControl: CacheControl = CacheControl.Builder()
            .maxStale(3, TimeUnit.DAYS)
            .build()

        // тут треба прокидувати правильне значення
        if (false) {
            request = request.newBuilder()
                //.header(CACHE_CONTROL_HEADER, "public, only-if-cached, max-stale=$maxStale")
                .header(CACHE_CONTROL_HEADER, cacheControl.toString())
                .removeHeader(PRAGMA_HEADER)
                .build()
        }
        chain.proceed(request)
    }

fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(ResultAdapterFactory())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()