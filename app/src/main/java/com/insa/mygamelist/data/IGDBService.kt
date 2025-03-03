package com.insa.mygamelist.data

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.Call
import retrofit2.Response


@Serializable
data class Game1(
    val id: Long,
    val name: String,
    val genres: List<Int> = emptyList(),
    val cover: Int?,
    val first_release_date: Long?,
    val platforms: List<Int> = emptyList(),
    val summary: String?,
    val total_rating: Double?
)

interface IGDBService {
    @Headers(
        "Client-ID: qlo915d37u9c6cdlq0i1tk0y50q2z6",
        "Authorization: Bearer q7yklh4qqs4g4ztqac3h8h09io5qb8",
        "Content-Type: application/json"
    )
    @POST("games")
    suspend fun getGames(@Body body: String): List<Game>
}
