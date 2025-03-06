package com.insa.mygamelist.data


import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface IGDBService {

    @POST("games")
    suspend fun getGames(@Header("Authorization") auth: String, @Header("Client-ID") clientId: String, @Body body: RequestBody): List<Game>

    @POST("genres")
    suspend fun getGenres(@Header("Authorization") auth: String,@Header("Client-ID") clientId: String, @Body body: RequestBody): List<Genre>?

    @POST("covers")
    suspend fun getCovers(@Header("Authorization") auth: String,@Header("Client-ID") clientId: String, @Body body: RequestBody): List<Cover>?

    @POST("platforms")
    suspend fun getPlatforms(@Header("Authorization") auth: String,@Header("Client-ID") clientId: String, @Body body: RequestBody): List<Platform>?

    @POST("platform_logos")
    suspend fun getPlatformLogos(@Header("Authorization") auth: String,@Header("Client-ID") clientId: String, @Body body: RequestBody): List<PlatformLogo>?
}
