package com.insa.mygamelist

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


private val Context.dataStore by preferencesDataStore(name = "favorites_prefs")

class GererFavoris(private val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: GererFavoris
        private val FAVORITES_KEY = stringSetPreferencesKey("favorite_games")

        fun init(context: Context) {
            instance = GererFavoris(context)
        }

        val favoriteGames: Flow<Set<Long>>
            get() = instance.context.dataStore.data.map { preferences ->
                preferences[FAVORITES_KEY]?.map { it.toLong() }?.toSet() ?: emptySet()
            }

        fun toggleFavorite(gameId: Long, coroutineScope: CoroutineScope) {
            coroutineScope.launch {
                instance.context.dataStore.edit { preferences ->
                    val currentFavorites =
                        preferences[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
                    if (currentFavorites.contains(gameId.toString())) { // Si on a déjà le jeu on l'enlève de la liste des favoris sinon on la rajoute
                        currentFavorites.remove(gameId.toString())
                    } else {
                        currentFavorites.add(gameId.toString())
                    }
                    preferences[FAVORITES_KEY] = currentFavorites
                }
            }
        }
    }
}
/*var favoriteGames = mutableStateListOf<Long>()

fun toggleFavorite(gameId: Long) {
    if (favoriteGames.contains(gameId)) {
        favoriteGames.remove(gameId)
    } else {
        favoriteGames.add(gameId)
    }
}

*/