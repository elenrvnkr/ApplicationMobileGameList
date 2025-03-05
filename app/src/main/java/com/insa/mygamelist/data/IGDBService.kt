package com.insa.mygamelist.data

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IGDBService {
    @POST("games")
    suspend fun getGames(@Header("Authorization") auth: String, @Header("Client-ID") clientId: String, @Body body: String): List<Game>?

    @POST("genres")
    suspend fun getGenres(@Header("Authorization") auth: String,@Header("Client-ID") clientId: String, @Body body: String): List<Genre>?

    @POST("covers")
    suspend fun getCovers(@Header("Authorization") auth: String,@Header("Client-ID") clientId: String, @Body body: String): List<Cover>?

    @POST("platforms")
    suspend fun getPlatforms(@Header("Authorization") auth: String,@Header("Client-ID") clientId: String, @Body body: String): List<Platform>?

    @POST("platform_logos")
    suspend fun getPlatformLogos(@Header("Authorization") auth: String,@Header("Client-ID") clientId: String, @Body body: String): List<PlatformLogo>?
}