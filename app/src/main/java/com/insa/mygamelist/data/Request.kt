package com.insa.mygamelist.data


import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IGDBService {

    @POST("games")
    suspend fun getGames(
        @Header("Authorization") auth: String,
        @Header("Client-ID") clientId: String,
        @Body body: RequestBody
    ): List<Game>

}

object RetrofitClient {
    private const val BASE_URL = "https://api.igdb.com/v4/"

    val instance: IGDBService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IGDBService::class.java)
    }
}