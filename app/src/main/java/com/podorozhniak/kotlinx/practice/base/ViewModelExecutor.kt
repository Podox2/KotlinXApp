package com.podorozhniak.kotlinx.practice.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicInteger

class ViewModelExecutor {

    var onError: (Throwable) -> Unit = ::logError

    private fun logError(exc: Throwable) {
        Log.d("ERROR", "${exc.localizedMessage}")
    }

    //val lastOperationThrowable = Event<Throwable?>()

    private var loadingOperationCount = AtomicInteger(0)

    private val loadingOperationCountLiveData = MutableLiveData(loadingOperationCount.get())

    //val hasLoading: LiveData<Boolean> = mapLiveData(loadingOperationCountLiveData) { it > 0 }

    suspend fun <T> getSafe(
        operation: suspend () -> T
    ): T = operation()

    suspend fun <T> runSafe(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit
    ) {
        runSafe(
            operation = operation,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    suspend fun <T> runWithoutLoading(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit
    ) {
        runSafe(
            operation = operation,
            onSuccess = onSuccess,
            onError = onError,
            onStartLoading = null,
            onEndLoading = null,
        )
    }

    suspend fun <T> runSafe(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onStartLoading: (() -> Unit)? = ::registerLoading,
        onEndLoading: (() -> Unit)? = ::unregisterLoading,
    ) {
        runCatching {
            onStartLoading?.invoke()
            operation()
        }.onSuccess {
            onEndLoading?.invoke()
            //lastOperationThrowable.value = null
            onSuccess(it)
        }.onFailure {
            onEndLoading?.invoke()
            //lastOperationThrowable.value = it
            onError(it)
        }
    }

    private fun registerLoading() {
        loadingOperationCountLiveData.value = loadingOperationCount.incrementAndGet()
    }

    private fun unregisterLoading() {
        loadingOperationCountLiveData.value = loadingOperationCount.decrementAndGet()
    }
}