package com.insa.mygamelist.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IGDBClient {
    private const val BASE_URL = "https://api.igdb.com/v4/"

    val api: IGDBService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IGDBService::class.java)
    }
}