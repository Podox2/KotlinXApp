package com.podorozhniak.kotlinx.practice.extensions

import android.content.Context
import org.koin.java.KoinJavaComponent

val appContext: Context = KoinJavaComponent.get(Context::class.java)