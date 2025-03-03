package com.insa.mygamelist.screen

import com.insa.mygamelist.data.Game
import com.insa.mygamelist.data.IGDBApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun fetchGames(clientId: String, authToken: String): List<Game> {
    return withContext(Dispatchers.IO) {
        IGDBApi.service.getGames(clientId, "Bearer $authToken")
    }
}
