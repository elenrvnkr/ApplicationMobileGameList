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

    fun loadMoreGames() {
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

fun GameEntity.toGame(): Game {
    return Game(
        id = this.id,
        name = this.name,
        cover = mapOf("image_id" to (this.coverUrl ?: "")),
        first_release_date = this.releaseDate ?: 0L,
        genres = this.genres?.split(";")?.map { mapOf("name" to it) } ?: listOf(mapOf("name" to "Non disponible")),
        platforms = this.platforms?.split(";")?.mapIndexed { index, platformName ->
            val logos = this.platform_logos?.split(";") ?: emptyList()
            Platform(null, platformName, PlatformLogo(null, logos.getOrNull(index)))
        } ?: emptyList()
        ,
        summary = this.summary ?: "Résumé par défaut",
        total_rating = this.rating ?: ""
    )
}

fun Game.toGameEntity(): GameEntity {
    return GameEntity(
        id = id,
        name = name ?: "",
        summary = summary ?: null,
        rating = total_rating?.toString() ?: "0",
        coverUrl = cover?.get("image_id") ?: null,
        genres = genres?.joinToString(", ") { it["name"] ?: "" } ?: null,
        platforms = platforms?.joinToString(",") { it.name ?: "" } ?: "",
        platform_logos = platforms
            ?.mapNotNull { it.platform_logo?.image_id }
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString(",")
            ?: "",
        releaseDate = first_release_date
    )
}

data class Game(val id: Long, val cover: Map<String, String>, val first_release_date: Long, val genres: List<Map<String,String>>, val name: String, val platforms: List<Platform>, val summary: String, val total_rating: String)
data class PlatformLogo(
    val id: Int?,
    val image_id: String?
)

data class Platform(
    val id: Int?,
    val name: String?,
    val platform_logo: PlatformLogo?
)