package com.insa.mygamelist.data

import android.content.Context
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
    var covers = mutableStateListOf<Cover>()
    var genres = mutableStateListOf<Genre>()
    var games = mutableStateListOf<Game>()
    var platforms = mutableStateListOf<Platform>()
    var platformlogos = mutableStateListOf<PlatformLogo>()
    var platformetlogo = mutableStateListOf<Platformetlogo>()

    var isLoading = mutableStateOf(false)

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    fun load(context: Context) {

        scope.launch {
            withContext(Dispatchers.Main) {
                isLoading.value = true // Activer le chargement
            }
            try {
                val gamesResponse = fetchGames()
                val genresResponse = fetchGenres()
                val coversResponse = fetchCovers()
                val platformsResponse = fetchPlatforms()
                val platformLogosResponse = fetchPlatformLogos()

                withContext(Dispatchers.Main) {
                    updateUI(gamesResponse, genresResponse, coversResponse, platformsResponse, platformLogosResponse)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erreur de chargement des données: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }finally {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                }
        }}
    }

    private suspend fun fetchGames(): List<Game> {
        val body = "fields id, cover, first_release_date, genres, name, platforms, summary, total_rating; limit 500;"
        val requestBody = body.toRequestBody("text/plain".toMediaType())
        return RetrofitClient.instance.getGames("Bearer $bearertoken", clientId = clientid, body = requestBody) ?: emptyList()

    }

    private suspend fun fetchGenres(): List<Genre> {
        val body = "fields id, name; limit 500; sort rating desc;"
        val requestBody = body.toRequestBody("text/plain".toMediaType())
        return RetrofitClient.instance.getGenres("Bearer $bearertoken", clientId = clientid, requestBody) ?: emptyList()
    }

    private suspend fun fetchCovers(): List<Cover> {
        val body = "fields id, url; limit 500; sort rating desc;"
        val requestBody = body.toRequestBody("text/plain".toMediaType())
        return RetrofitClient.instance.getCovers("Bearer $bearertoken", clientId = clientid, requestBody) ?: emptyList()
    }

    private suspend fun fetchPlatforms(): List<Platform> {
        val body = "fields id, name, platform_logo; limit 500;"
        val requestBody = body.toRequestBody("text/plain".toMediaType())
        return RetrofitClient.instance.getPlatforms("Bearer $bearertoken", clientId = clientid, requestBody) ?: emptyList()
    }

    private suspend fun fetchPlatformLogos(): List<PlatformLogo> {
        val body = "fields id, url; limit 500;"
        val requestBody = body.toRequestBody("text/plain".toMediaType())
        return RetrofitClient.instance.getPlatformLogos("Bearer $bearertoken", clientId = clientid, requestBody) ?: emptyList()
    }

    private fun updateUI(
        gamesResponse: List<Game>,
        genresResponse: List<Genre>,
        coversResponse: List<Cover>,
        platformsResponse: List<Platform>,
        platformLogosResponse: List<PlatformLogo>
    ) {
        games.clear()
        games.addAll(gamesResponse)


        genres.clear()
        genres.addAll(genresResponse)

        covers.clear()
        covers.addAll(coversResponse)

        platforms.clear()
        platforms.addAll(platformsResponse)

        platformlogos.clear()
        platformlogos.addAll(platformLogosResponse)

        platformetlogo.clear()
        platformetlogo.addAll(platforms.flatMap { platform ->
            platformlogos.filter { it.id == platform.platform_logo }
                .map { Platformetlogo(platform.id, platform.name, platform.platform_logo, it.url) }
        })


    }
}


/*
object IGDB {

    lateinit var covers: List<Cover>
    lateinit var genres : List<Genre>
    lateinit var games : List<Game>
    lateinit var platforms : List<Platform>
    var platformetlogo = mutableListOf<Platformetlogo>()
    lateinit var platformlogos : List<PlatformLogo>

    fun load(context: Context){
        IGDBWrapper.setCredentials(clientid, bearertoken)
        val apicalypse = APICalypse().fields("*")

        try{
            covers = IGDBWrapper.covers(apicalypse)
        } catch(e: RequestException) {
            Log.d(e.message,"cover")
            print(e.message)
        }
        try{
            games = IGDBWrapper.games(apicalypse)
        } catch(e: RequestException) {
            print("game")
            print(e.message)
        }
        try{
            genres = IGDBWrapper.genres(apicalypse)
        } catch(e: RequestException) {
            print("genre")
            print(e.message)
        }
        try{
            platforms = IGDBWrapper.platforms(apicalypse)
        } catch(e: RequestException) {
            print("platform")
            print(e.message)
        }
        try{
            platformlogos = IGDBWrapper.platformLogos(apicalypse)
        } catch(e: RequestException) {
            print("platform logo")
            print(e.message)
        }
        for (platform in platforms){
            for (platforml in platformlogos){
                if (platform.id == platforml.id){
                    platformetlogo.add(Platformetlogo(platform.id,platform.name,platform.id,platforml.url))             }
            }
        }
    }
}
*/
/*
object IGDB {

    lateinit var covers: List<Cover>
    lateinit var genres : List<Genre>
    lateinit var games : List<Game>
    lateinit var platforms : List<Platform>
    var platformetlogo = mutableListOf<Platformetlogo>()
    lateinit var platformlogos : List<PlatformLogo>

    fun load(context: Context) {
        val coversFromJson: List<Cover> = Gson().fromJson(
            context.resources.openRawResource(R.raw.covers).bufferedReader(),
            object : TypeToken<List<Cover>>() {}.type

        )
        val genresFromJson: List<Genre> = Gson().fromJson(
            context.resources.openRawResource(R.raw.genres).bufferedReader(),
            object : TypeToken<List<Genre>>() {}.type

        )
        val gamesFromJson: List<Game> = Gson().fromJson(
            context.resources.openRawResource(R.raw.games).bufferedReader(),
            object : TypeToken<List<Game>>() {}.type

        )
        val platformsFromJson: List<Platform> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platform>>() {}.type
        )
        val platformsLogoFromJson: List<PlatformLogo> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformLogo>>() {}.type
        )

        covers = coversFromJson
        genres = genresFromJson
        games = gamesFromJson
        platforms = platformsFromJson
        platformlogos = platformsLogoFromJson

        for (platform in platforms){
            for (platforml in platformlogos){
                if (platform.platform_logo == platforml.id){
                    platformetlogo.add(Platformetlogo(platform.id,platform.name,platform.platform_logo,platforml.url))             }
            }
        }
    }
}
*/
data class Cover(val id: Long, val url: String)
data class Genre(val id: Long, val name: String)
data class Game(val id: Long, val cover: Long, val first_release_date: Long, val genres: List<Long>, val name: String, val platforms: List<Long>, val summary: String, val total_rating: String)
data class Platform(val id: Long, val name: String, val platform_logo: Long)
data class Platformetlogo(val id: Long, val name: String, val platform_logo: Long, val url:String)
data class PlatformLogo(val id: Long, val url: String)