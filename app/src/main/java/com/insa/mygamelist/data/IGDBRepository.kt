package com.insa.mygamelist.data

class IGDBRepository {

    suspend fun getGames(): List<Game> {
        val requestBody = """
            fields name, cover, first_release_date, genres, platforms, summary, total_rating;
            limit 50;
        """.trimIndent()

        return IGDBClient.api.getGames(requestBody)
    }
}