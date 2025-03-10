package com.insa.mygamelist.screen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.insa.mygamelist.R
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.data.IGDB.isLoading2


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameList(navController: NavController, favoriteGames: List<Long>) {
    val isLoading by IGDB.isLoading

    if (isLoading) {
        LoadingScreen()
    } else {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchVisible by rememberSaveable { mutableStateOf(false) }
        var showOnlyFavorites by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Color(0, 109, 119),
                titleContentColor = Color(237, 246, 255)
            ),
            title = {
                AnimatedVisibility(visible = !isSearchVisible) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logoappli),
                            contentDescription = "Logo de l'application",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Le Grenier du Joueur")
                    }
                }

                AnimatedVisibility(visible = isSearchVisible) {
                    SearchBar(searchQuery, { searchQuery = it })
                }
            },
            actions = {
                IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = if(isSearchVisible){ "Désactiver la barre de recherche" }else{ "Activer la barre de recherche" },
                        tint = Color(237, 246, 255)
                    )
                }
                IconButton(onClick = {showOnlyFavorites = !showOnlyFavorites}) {
                    Icon(
                        imageVector = if(showOnlyFavorites){ Icons.Filled.Star }else{ Icons.TwoTone.Star },
            contentDescription = if(showOnlyFavorites){ "Voir tout les jeux" }else{ "Filtrer les favoris" },
                        tint = if (showOnlyFavorites) Color(237, 246, 255) else Color(237, 246, 255)
                    )
                }
            })
    }, modifier = Modifier.fillMaxSize().background(Color(237, 246, 255))) { innerPadding ->
        val filteredGames = IGDB.games.filter { game ->
            (game.name?.contains(searchQuery, ignoreCase = true) == true) ||
                    (game.genres ?: emptyList()).any { genre ->
                        (genre["name"] ?: "").contains(searchQuery, ignoreCase = true)
                    } ||
                    game.platforms?.any { platform ->
                        platform.name?.contains(searchQuery, ignoreCase = true) == true
                    } == true
        }.filter{ game -> if(showOnlyFavorites ){
            favoriteGames?.contains(game.id) == true

        }else{
            true
        }
        }
        if (filteredGames.isEmpty()) {
            NoMatchScreen()
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding).background(Color(131, 197, 190))) {
                items(filteredGames.size) { index ->

                    GameCase(navController, favoriteGames, filteredGames, index)

                }

                item {
                    if (isLoading2.value) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = Color(237, 246, 255))
                            Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "Chargement en cours...", color = Color(237, 246, 255), style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        LaunchedEffect(Unit) {
                            IGDB.loadMoreGames()
                        }
                    }
                }
            }
        }
    }
}}



