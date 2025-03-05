package com.insa.mygamelist.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygamelist.R


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
var isloading by mutableStateOf(false)


object IGDB {
    var covers by mutableStateOf<List<Cover>>(emptyList())
    var genres by mutableStateOf<List<Genre>>(emptyList())
    var games by mutableStateOf<List<Game>>(emptyList())
    var platforms by mutableStateOf<List<Platform>>(emptyList())
    var platformetlogo by mutableStateOf<List<Platformetlogo>>(emptyList())
    var platformlogos by mutableStateOf<List<PlatformLogo>>(emptyList())

    fun load(context: Context, authToken: String) {
        isloading = true
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val gamesResponse = RetrofitClient.instance.getGames("Bearer $authToken",clientId = clientid, "fields *;limit 100;")
                val genresResponse = RetrofitClient.instance.getGenres("Bearer $authToken", clientId = clientid,"fields *;limit 100;")
                val coversResponse = RetrofitClient.instance.getCovers("Bearer $authToken", clientId = clientid,"fields *;limit 100;")
                val platformsResponse = RetrofitClient.instance.getPlatforms("Bearer $authToken", clientId = clientid,"fields *;limit 100;")
                val platformLogosResponse = RetrofitClient.instance.getPlatformLogos("Bearer $authToken", clientId = clientid,"fields *;limit 100;")

                withContext(Dispatchers.Main) {


                // Mettre à jour les propriétés
                    games = gamesResponse ?: emptyList()
                    genres = genresResponse ?: emptyList()
                    covers = coversResponse ?: emptyList()
                    platforms = platformsResponse ?: emptyList()
                    platformlogos = platformLogosResponse ?: emptyList()


                    // Mettre à jour platformetlogo
                platformetlogo = platforms.flatMap { platform ->
                    platformlogos.filter { it.id == platform.platform_logo }
                        .map { Platformetlogo(platform.id, platform.name, platform.platform_logo, it.url) }
                }
                    isloading = false
            }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erreur de chargement des données", Toast.LENGTH_SHORT).show()
                    isloading = false
                }
            }
        }
    }
}
*/
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

data class Cover(val id: Long, val url: String)
data class Genre(val id: Long, val name: String)
data class Game(val id: Long, val cover: Long, val first_release_date: Long, val genres: List<Long>, val name: String, val platforms: List<Long>, val summary: String, val total_rating: String)
data class Platform(val id: Long, val name: String, val platform_logo: Long)
data class Platformetlogo(val id: Long, val name: String, val platform_logo: Long, val url:String)
data class PlatformLogo(val id: Long, val url: String)