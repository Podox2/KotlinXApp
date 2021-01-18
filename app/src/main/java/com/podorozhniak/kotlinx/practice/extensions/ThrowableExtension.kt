package com.podorozhniak.kotlinx.practice.extensions

import android.util.Log
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val TAG_THROWABLE = "TAG_THROWABLE"

fun Throwable.exceptionHandler() {
    when (this) {
        is SocketTimeoutException -> {
            Log.d(TAG_THROWABLE, "SocketTimeoutException")
        }
        is UnknownHostException -> {
            Log.d(TAG_THROWABLE, "UnknownHostException")
        }
        is HttpException -> {
            /*val error = Gson().fromJson(
                this.response()?.errorBody()?.string(),
                ErrorResponse::class.java
            )*/
            Log.d(TAG_THROWABLE, "HttpException")
        }

        /*is StripeException -> {
            Timber.e("StripeException ${this.message ?: "No message"}")
            this.message?.let { showMessage(it) }
        }
        is CardException -> {
            Timber.e("CardException ${this.message ?: "No message"}")
            this.message?.let { showMessage(it) }
        }*/
        else -> {
            Log.d(TAG_THROWABLE, "else")
        }
    }
}