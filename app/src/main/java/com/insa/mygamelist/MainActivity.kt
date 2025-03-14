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

        IGDB.initDatabase(this) //Récupère la database
        IGDB.load(this) // Récupère les jeux chargés depuis l'IGDB
        GererFavoris.init(this) // Récupère les favoris

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


                    NavHost(navController, startDestination = GameListe) { // Le navController permet de revenir en arrière, j(indique que je pars de la gameliste et j'indique les routes qu'il peut prendre
                        composable<GameListe> {
                            GameList(
                                navController, favoriteGames) }
                        composable<GameInfor> {
                            val game = it.toRoute<GameInfor>()
                            GameInfo(
                                navController,
                                gamei = game, favoriteGames)
                        }
                    }
                }
            }

        }

    }




