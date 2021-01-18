package com.podorozhniak.kotlinx.practice.extensions.coroutines

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

fun <R> CoroutineScope.launchWithHandling(
    liveData: MutableLiveData<LoadingState> = MutableLiveData(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    errorHandler: ErrorHandler = DefaultErrorHandler,
    onSuccess: suspend (R) -> Unit = {},
    onError: suspend (Throwable) -> Unit = {},
    doBlock: suspend () -> R
) {
    liveData.value = LoadingState.Loading
    val startedCoroutineScope = this

    launch(dispatcher) {
        kotlin.runCatching {
            doBlock()
        }.onFailure {
            errorHandler.handle(liveData, it) {
                startedCoroutineScope
                    .launchWithHandling(liveData, dispatcher, errorHandler, onSuccess, onError, doBlock)
            }
            onError(it)
        }.onSuccess {
            liveData.postValue(LoadingState.Normal)
            onSuccess(it)
        }
    }
}

fun <R> CoroutineScope.launchWithHandlingIO(
    liveData: MutableLiveData<LoadingState> = MutableLiveData(),
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    errorHandler: ErrorHandler = DefaultErrorHandler,
    doBlock: suspend () -> R,
    onSuccess: suspend (R) -> Unit = {},
    onError: suspend (Throwable) -> Unit = {}
) {
    liveData.postValue(LoadingState.Loading)
    val startedCoroutineScope = this

    launch(dispatcher) {
        kotlin.runCatching {
            doBlock()
        }.onFailure {
            errorHandler.handle(liveData, it) {
                startedCoroutineScope
                    .launchWithHandling(liveData, dispatcher, errorHandler, onSuccess, onError, doBlock)
            }
            onError(it)
        }.onSuccess {
            liveData.postValue(LoadingState.Normal)
            onSuccess(it)
        }
    }
}
