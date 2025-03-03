package com.insa.mygamelist.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IGDBApiService {
    @GET("games")
    suspend fun getGames(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authToken: String,
        @Query("fields") fields: String = "id,name,cover,first_release_date,genres,platforms,summary,total_rating"
    ): List<Game>

    @GET("covers")
    suspend fun getCovers(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authToken: String,
        @Query("fields") fields: String = "id,url"
    ): List<Cover>

    @GET("genres")
    suspend fun getGenres(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authToken: String,
        @Query("fields") fields: String = "id,name"
    ): List<Genre>

    @GET("platforms")
    suspend fun getPlatforms(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authToken: String,
        @Query("fields") fields: String = "id,name,platform_logo"
    ): List<Platform>

    @GET("platform_logos")
    suspend fun getPlatformLogos(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authToken: String,
        @Query("fields") fields: String = "id,url"
    ): List<PlatformLogo>
}
