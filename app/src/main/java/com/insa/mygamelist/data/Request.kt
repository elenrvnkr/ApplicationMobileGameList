package com.insa.mygamelist.data

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Client-ID", clientid)
            .addHeader("Authorization", "Bearer $bearertoken")
            .build()
        return chain.proceed(request)
    }
}
