package com.insa.mygamelist

import android.os.Bundle
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

@Serializable
object GameListe
@Serializable
data class GameInfor(val id :Long)




class MainActivity : ComponentActivity() {

    private val favoriteGames = mutableStateListOf<Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        IGDB.initDatabase(this)
        IGDB.load(this)
        GererFavoris.init(this)

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




