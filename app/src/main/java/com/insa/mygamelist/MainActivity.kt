package com.insa.mygamelist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.screen.GameInfo
import com.insa.mygamelist.screen.GameList
import com.insa.mygamelist.ui.theme.MyGamesListTheme
import kotlinx.serialization.Serializable
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Date


@Serializable
object GameListe
@Serializable
data class GameInfor(val id :Long)


//remettre bien tout dans database
//bouton pour revenir en haut de l'appli

class MainActivity : ComponentActivity() {

    private val favoriteGames = mutableStateListOf<Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.initDatabase(this)
        IGDB.load(this) // Charge le token
        GererFavoris.init(this)
        Log.d("date", Date().time.toString())
        Log.d("system milis",((System.currentTimeMillis() / 1000).toString()))

        lifecycleScope.launch {
            GererFavoris.favoriteGames.collect { favorites ->
                favoriteGames.clear()
                favoriteGames.addAll(favorites)
            }
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MyGamesListTheme {


                    NavHost(navController, startDestination = GameListe) {
                        composable<GameListe> {
                            GameList(
                                navController, favoriteGames) }
                        composable<GameInfor> {
                            val game = it.toRoute<GameInfor>()
                            GameInfo(
                                navController,
                                gamei = game, favoriteGames)}
                    }
                }
            }

        }

    }




