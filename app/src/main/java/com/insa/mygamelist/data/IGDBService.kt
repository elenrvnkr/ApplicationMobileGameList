package com.insa.mygamelist.data


import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IGDBService {

    @POST("games")
    suspend fun getGames(@Header("Authorization") auth: String, @Header("Client-ID") clientId: String, @Body body: RequestBody): List<Game>

}
