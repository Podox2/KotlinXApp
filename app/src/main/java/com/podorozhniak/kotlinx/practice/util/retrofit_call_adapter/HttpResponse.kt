package com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}
