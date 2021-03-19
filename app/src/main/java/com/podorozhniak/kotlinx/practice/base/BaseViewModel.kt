package com.podorozhniak.kotlinx.practice.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel(private val viewModelExecutor: ViewModelExecutor) : ViewModel() {
    protected suspend fun <T> runSafe(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit
    ) {
        viewModelExecutor.runSafe(operation, onSuccess)
    }

    protected suspend fun <T> runSafe(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = ::onError,
    ) {
        viewModelExecutor.runSafe(operation, onSuccess, onError)
    }

    protected open fun onError(throwable: Throwable) {
        /*when {
            isUnauthorizedThrowable(throwable) -> {
                handleUnauthorizedThrowable()
            }
            throwable is ResponseContainsErrorException -> {
                lastOperationThrowable.value = Exception(throwable.error.errorMessage)
            }
            else -> Timber.e(throwable)
        }*/
    }
}

