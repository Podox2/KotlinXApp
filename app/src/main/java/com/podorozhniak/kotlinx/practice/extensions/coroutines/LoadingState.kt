package com.podorozhniak.kotlinx.practice.extensions.coroutines

sealed class LoadingState {
    object Normal : LoadingState()
    object Loading : LoadingState()
    data class Error(val error: CustomError) : LoadingState()
}
