package com.insa.mygamelist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insa.mygamelist.data.IGDBService
import com.insa.mygamelist.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
/*
class GameViewModel : ViewModel() {



    companion object {
        private val _games = MutableStateFlow<List<Game>>(emptyList())
        val games = _games.asStateFlow()

        private val _covers = MutableStateFlow<List<Cover>>(emptyList())
        val covers = _covers.asStateFlow()

        private val _genres = MutableStateFlow<List<Genre>>(emptyList())
        val genres = _genres.asStateFlow()

        private val _platforms = MutableStateFlow<List<Platform>>(emptyList())
        val platforms = _platforms.asStateFlow()

        private val _platformLogos = MutableStateFlow<List<PlatformLogo>>(emptyList())
        val platformLogos = _platformLogos.asStateFlow()
    }




    init {
        println("Initialisation du ViewModel")
        fetchGames()
        fetchCovers()
        fetchGenres()
        fetchPlatforms()
        fetchPlatformLogos()
    }



    private fun fetchGames() {
        viewModelScope.launch {
            try {
                println("Début de la requête games")
                val response = IGDBService.api.getGames(
                    "fields id, name, cover, first_release_date, genres, platforms, summary, total_rating; limit 50;"
                )
                if (response.isSuccessful) {
                    val games = response.body()
                    println("Données reçues : ${games?.size} jeux")
                    _games.value = games ?: emptyList()
                } else {
                    println("Erreur: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
    }

    private fun fetchCovers() {
        viewModelScope.launch {
            try {
                val response = IGDBService.api.getCovers("fields id, url; limit 50;")
                if (response.isSuccessful) {
                    _covers.value = response.body() ?: emptyList()
                } else {
                    println("Erreur: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            try {
                val response = IGDBService.api.getGenres("fields id, name; limit 50;")
                if (response.isSuccessful) {
                    _genres.value = response.body() ?: emptyList()
                } else {
                    println("Erreur: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
    }

    private fun fetchPlatforms() {
        viewModelScope.launch {
            try {
                val response = IGDBService.api.getPlatforms("fields id, name, platform_logo; limit 50;")
                if (response.isSuccessful) {
                    _platforms.value = response.body() ?: emptyList()
                } else {
                    println("Erreur: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }

        }
    }

    private fun fetchPlatformLogos() {
        viewModelScope.launch {
            try {
                val response = IGDBService.api.getPlatformLogos("fields id, url; limit 50;")
                if (response.isSuccessful) {
                    _platformLogos.value = response.body() ?: emptyList()
                } else {
                    println("Erreur: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
    }

}
*/