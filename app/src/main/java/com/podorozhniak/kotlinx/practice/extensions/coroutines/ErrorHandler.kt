package com.podorozhniak.kotlinx.practice.extensions.coroutines

import android.util.Log
import androidx.lifecycle.MutableLiveData

interface ErrorHandler {
    fun handle(liveData: MutableLiveData<LoadingState>, t: Throwable, action: () -> Unit) {
        Log.w(
            t::class.java.simpleName,
            t.localizedMessage ?: t.message
                ?: OOOPS
        )
    }
    companion object {
        const val OOOPS = "Ooops"
    }
}
