package com.insa.mygamelist.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.GameInfor
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.data.bearertoken
import com.insa.mygamelist.favoriteGames


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameList(navController: NavController) {

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = Color(144, 122, 200),
                titleContentColor = Color.Black
            ),
            title = {
                if (isSearchVisible) {
                    SearchBar(searchQuery, { searchQuery = it })
                } else {
                    Text("Le Grenier du Joueur")
                }
            },
            actions = {
                IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Barre de recherche"
                    )
                }
            })
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        val filteredGames = IGDB.games.filter { game ->
            game.name?.contains(searchQuery, ignoreCase = true) == true ||
                    IGDB.genres?.any { it.id in game.genres && it.name?.contains(searchQuery, ignoreCase = true) == true } == true ||
                    IGDB.platforms?.any { it.id in game.platforms && it.name?.contains(searchQuery, ignoreCase = true) == true } == true
        }
        if (filteredGames.isEmpty()) {
            NoMatchScreen()
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(filteredGames.size) { index ->
                    val isFavorite = favoriteGames.contains(filteredGames[index].id)
                    Box(
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.LightGray)
                                .padding(5.dp)
                                .clickable { navController.navigate(GameInfor(filteredGames[index].id)) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.padding(2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = "https:${IGDB.covers.find { it.id == filteredGames[index].cover }?.url}",
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentDescription = null
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = filteredGames[index].name ?: "Valeur par défaut",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W600,
                                    color = Color.Black,
                                    style = TextStyle(textDecoration = TextDecoration.Underline),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(5.dp)
                                )
                                Text(text = "Genres : " + IGDB.genres.filter { it.id in filteredGames[index].genres }
                                    .joinToString(separator = ", ") { it.name },
                                    maxLines = 2,
                                    color = Color.Black,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(5.dp)
                                )

                            }
                            Column {
                                IconButton(
                                    onClick = {
                                        if (favoriteGames.contains(filteredGames[index].id)) {
                                            favoriteGames.remove(filteredGames[index].id)
                                        } else {
                                            favoriteGames.add(filteredGames[index].id)
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.TwoTone.Star,
                                        contentDescription = "Favori",
                                        tint = if (isFavorite) Color(255, 222, 33) else Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

