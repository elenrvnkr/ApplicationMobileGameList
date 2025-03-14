package com.insa.mygamelist.data


import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
        val logging = HttpLoggingInterceptor().apply { //voir ce que je reçois de l'API
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


suspend fun fetchGames(offset: Int): List<Game> { //va demander ma requête à l'IGDBD
    val token = TokenManager.getToken() // je prends mon token
    val body = "fields id, cover.image_id, first_release_date, genres.name, name, platforms.platform_logo.image_id, platforms.name, summary, total_rating; limit 500; offset $offset;"
    val requestBody = body.toRequestBody("text/plain".toMediaType()) // Si je donne tout en string ça marche pas........
    return RetrofitClient.instance.getGames("Bearer $token", clientId = clientid, body = requestBody) ?: emptyList()

}
