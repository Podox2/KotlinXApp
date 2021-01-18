package com.podorozhniak.kotlinx.practice.extensions.coroutines

import retrofit2.HttpException

sealed class CustomError {
    data class ConnectionProblem(val action: () -> Unit) : CustomError()
    data class HttpAuthError(val httpException: HttpException) : CustomError()
    data class HttpError(val httpException: HttpException) : CustomError()
    data class OtherError(val exception: Throwable) : CustomError()

    data class EmptyPurchaseListException(val message: String) : CustomError()
    data class NoSkuDetails(val message: String) : CustomError()
    /*data class BillingError(val message: String, val billingException: BillingException) :
        CustomError()*/
}
