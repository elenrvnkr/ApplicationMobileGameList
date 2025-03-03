package com.insa.mygamelist.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IGDBApi {
    private const val BASE_URL = "https://api.igdb.com/v4/"

    val service: IGDBApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IGDBApiService::class.java)
    }
}
