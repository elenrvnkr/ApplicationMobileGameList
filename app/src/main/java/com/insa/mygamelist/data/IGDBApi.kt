package com.insa.mygamelist.data

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IGDBApi {

    @Headers("Accept: application/json")
    @POST("games")
    suspend fun getGames(@Body body: String): List<Game>

    @Headers("Accept: application/json")
    @POST("covers")
    suspend fun getCovers(@Body body: String): List<Cover>

    @Headers("Accept: application/json")
    @POST("genres")
    suspend fun getGenres(@Body body: String): List<Genre>

    @Headers("Accept: application/json")
    @POST("platforms")
    suspend fun getPlatforms(@Body body: String): List<Platform>

    @Headers("Accept: application/json")
    @POST("platform_logos")
    suspend fun getPlatformLogos(@Body body: String): List<PlatformLogo>
}