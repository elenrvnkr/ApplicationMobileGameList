package com.insa.mygamelist.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insa.mygamelist.GameInfor
import com.insa.mygamelist.GererFavoris
import com.insa.mygamelist.R
import com.insa.mygamelist.data.IGDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameInfo(navController: NavController, gamei: GameInfor, favoriteGames: List<Long>) {

    val jeu = IGDB.games.find { it.id == gamei.id }
    val isFavorite = jeu?.let { favoriteGames.contains(it.id) }
    Scaffold(
        topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = Color(0, 109, 119),
                titleContentColor = Color(237, 246, 255)
            ), title = {
                if (jeu != null) {
                    Text(text = jeu.name,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis)
                }
            },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color(237, 246, 255)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (jeu != null) {
                                GererFavoris.toggleFavorite(jeu.id)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite == true) Icons.Filled.Star else Icons.TwoTone.Star,
                            contentDescription = "Favori",
                            tint = if (isFavorite == true) Color(237, 246, 255) else Color(237, 246, 255)
                        )
                    }
                }

            )
        },
        modifier = Modifier.fillMaxSize().background(Color(237,246,255))
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(237,246,255))
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
                        color = Color(0, 109, 119),
                        fontWeight = FontWeight.W900,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                    if(jeu.cover != null ){
                        if(jeu.cover["image_id"] != ""){
                        val cover = jeu.cover["image_id"]
                    AsyncImage(
                        model = "https://images.igdb.com/igdb/image/upload/t_cover_big/$cover.jpg",
                        modifier = Modifier
                            .padding(10.dp)
                            .size(250.dp),
                        contentDescription = "No description available",
                    )}else{
                            Image(
                                painter = painterResource(R.drawable.no_image),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(250.dp),
                                contentDescription = "No image",
                            )
                        }}else{
                        Image(
                            painter = painterResource(R.drawable.no_image),
                            modifier = Modifier
                                .padding(10.dp)
                                .size(250.dp),
                            contentDescription = "No image",
                        )
                    }
                    Text(text = if (jeu.genres != null) {
                        "Genres : " + jeu.genres!!.joinToString(separator = ", ") { it["name"] ?: "Inconnu" }
                    } else {
                        "Genres : Non disponible"
                    },
                        modifier = Modifier.padding(2.dp),
                        fontStyle = FontStyle.Italic,
                        color = Color(0, 109, 119),
                        fontSize = 15.sp
                    )

                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(jeu.platforms?.size ?: 0) { index ->
                            val platform = jeu.platforms[index]
                            val imageUrl = platform.platform_logo?.image_id?.let {
                                "https://images.igdb.com/igdb/image/upload/t_logo_med/$it.png"
                            }

                            AsyncImage(
                                model = imageUrl,
                                modifier = Modifier
                                    .padding(5.dp),
                                contentDescription = jeu.platforms[index].name
                            )
                        }
                            }

                    Text(
                        text = jeu.summary?: "Résumé non disponible",
                        fontFamily = FontFamily.Serif,
                        color = Color(0, 109, 119),
                        modifier = Modifier.padding(10.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

}
