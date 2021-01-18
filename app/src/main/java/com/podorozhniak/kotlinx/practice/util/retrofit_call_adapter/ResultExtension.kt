package com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter

fun Result.Failure<*>.handle() {
    when(this) {
        is Result.Failure.Error -> {
            //toast(this.asFailure().toString())
        }
        is Result.Failure.HttpError -> {
            //toast("${this.asFailure()} + http error")
        }
        is Result.Failure.UnknownHostError -> {
            //toast("${this.asFailure()} + no host error")
        }
    }
}