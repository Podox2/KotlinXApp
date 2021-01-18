package com.podorozhniak.kotlinx.practice.extensions.coroutines

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.podorozhniak.kotlinx.practice.di.appContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object DefaultErrorHandler : ErrorHandler {

    override fun handle(liveData: MutableLiveData<LoadingState>, t: Throwable, action: () -> Unit) {
        val handledError = when (t) {
            is ConnectException, is UnknownHostException, is SocketTimeoutException/*, is ApolloNetworkException*/ -> {
                CustomError.ConnectionProblem(action)
            }
            is HttpException -> when (t.code()) {
                401 -> CustomError.HttpAuthError(t)
                else -> CustomError.HttpError(t)
            }
            is IllegalStateException -> {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(appContext, "LOOPER", Toast.LENGTH_SHORT).show()
                }
                CustomError.OtherError(t)
            }
            /*is BillingException -> {
                val errorMessage = when (val er = CodeBillingResult.fromInt(t.code)) {
                    CodeBillingResult.USER_CANCELED -> "Canceled"
                    else -> t.message ?: OOOPS
                }
                CustomError.BillingError(errorMessage, t)
            }*/
            /*is EmptyPurchaseListException -> {
                CustomError.EmptyPurchaseListException(t.message ?: "No purchased items")
            }
            is NoSkuDetails -> {
                CustomError.NoSkuDetails("No sku details")
            }*/
            else -> CustomError.OtherError(t)
        }
        liveData.postValue(
            LoadingState.Error(
                handledError
            )
        )
    }
}
