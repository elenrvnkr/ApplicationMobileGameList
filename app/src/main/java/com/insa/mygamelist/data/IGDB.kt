package com.insa.mygamelist.data


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.SupervisorJob
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


object IGDB {

    var games = mutableStateListOf<Game>()

    var isLoading = mutableStateOf(false) //pour le démarrage

    var isLoading2= mutableStateOf(false) //pour le scrolling

    private lateinit var db: GameDatabase

    private var offset = 500
    private const val limit = 500

    fun initDatabase(context: Context) {
        db = GameDatabase.getDatabase(context)
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    fun load(context: Context) {
        scope.launch {
            withContext(Dispatchers.Main) {
                isLoading.value = true
            }

            try {
                TokenManager.init(context)
                val gamesResponse = fetchGames(0)
                saveToDatabase(gamesResponse)
                updateUI(gamesResponse)

                withContext(Dispatchers.Main) {
                    updateUI(gamesResponse)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val cachedGames = loadFromDatabase()
                if (cachedGames.isNotEmpty()) {
                    updateUI(cachedGames)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                    }}
            }finally {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                }
            }}
    }

    fun loadMoreGames() { //pour le scrolling infini
        if (isLoading2.value) return

        CoroutineScope(Dispatchers.IO).launch {
            isLoading2.value = true
            try {
                val newGames = fetchGames(offset)
                withContext(Dispatchers.Main) {
                    games.addAll(newGames)
                    offset += limit
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading2.value = false
            }
        }
    }

    private suspend fun fetchGames(offset: Int): List<Game> {
        val token = TokenManager.getToken()
        Log.d("token",token)
        val body = "fields id, cover.image_id, first_release_date, genres.name, name, platforms.platform_logo.image_id, platforms.name, summary, total_rating; limit 500; offset $offset;"
        val requestBody = body.toRequestBody("text/plain".toMediaType())
        return RetrofitClient.instance.getGames("Bearer $token", clientId = clientid, body = requestBody) ?: emptyList()

    }

    private suspend fun saveToDatabase(games: List<Game>) {
        val gameEntities = games.map { it.toGameEntity() }
        db.gameDao().insertGames(gameEntities)
    }

    private suspend fun loadFromDatabase(): List<Game> {
        return db.gameDao().getAllGames().map { it.toGame() }
    }

    private fun updateUI(
        gamesResponse: List<Game>
    ) {
        games.clear()
        games.addAll(gamesResponse)

    }
}
