package com.insa.mygamelist.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.GameInfor
import com.insa.mygamelist.data.IGDB
import com.insa.mygamelist.favoriteGames

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameInfo(navController: NavController, gamei: GameInfor) {

    val jeu = IGDB.games.find { it.id == gamei.id }
    val isFavorite = jeu?.let { favoriteGames.contains(it.id) }
    Scaffold(
        topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = Color(144, 122, 200),
                titleContentColor = Color.Black,
            ), title = {
                if (jeu != null) {
                    Text(text = jeu.name)
                }
            },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (jeu != null) {
                                if (favoriteGames.contains(jeu.id)) {
                                    favoriteGames.remove(jeu.id)
                                } else {
                                    favoriteGames.add(jeu.id)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite == true) Icons.Filled.Star else Icons.TwoTone.Star,
                            contentDescription = "Favori",
                            tint = if (isFavorite == true) Color(255, 222, 33) else Color.Black
                        )
                    }
                }

            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (jeu != null) {
                    Text(
                        text = jeu.name?: "Valeur par défaut",
                        modifier = Modifier.padding(10.dp),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W900,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                    AsyncImage(
                        model = "https:${IGDB.covers.find { it.id == jeu.cover }?.url}",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(250.dp),
                        contentDescription = null
                    )
                    Text(text = IGDB.genres.filter { it.id in jeu.genres}
                        .joinToString(separator = ", ") { it.name }?: "Valeur par défaut",
                        modifier = Modifier.padding(2.dp),
                        fontStyle = FontStyle.Italic,
                        fontSize = 15.sp
                    )
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(jeu.platforms.size) { index ->
                            AsyncImage(
                                model = "https:${IGDB.platformetlogo.find { it.id == jeu.platforms[index] }?.url}",
                                modifier = Modifier
                                    .padding(5.dp),
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        text = jeu.summary?: "Valeur par défaut",
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

}
