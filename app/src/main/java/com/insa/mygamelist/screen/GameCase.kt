package com.insa.mygamelist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.insa.mygamelist.data.Game

@Composable
fun GameCase(navController: NavController, favoriteGames: List<Long>, filteredGames: List<Game>, index: Int) {
    val isFavorite = favoriteGames.contains(filteredGames[index].id)
    Box{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(237, 246, 255))
                .padding(5.dp)
                .clickable { navController.navigate(GameInfor(filteredGames[index].id)) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(filteredGames[index].cover != null ){
                    if(filteredGames[index].cover["image_id"] != ""){
                        val cover = filteredGames[index].cover["image_id"]
                        AsyncImage(
                            model = "https://images.igdb.com/igdb/image/upload/t_cover_big/$cover.jpg",
                            modifier = Modifier
                                .padding(1.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentDescription = "Image illustrative du jeu",
                        )
                    }else{
                        Image(
                            painter = painterResource(R.drawable.no_image),
                            modifier = Modifier
                                .padding(1.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentDescription = "No image",
                        )
                    }}else{
                    Image(
                        painter = painterResource(R.drawable.no_image),
                        modifier = Modifier
                            .padding(1.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentDescription = "No image",
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
            ) {
                Text(
                    text = filteredGames[index].name ?: "Valeur par défaut",
                    fontSize = 20.sp,
                    color = Color(0,109,119),
                    fontWeight = FontWeight.W600,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(5.dp)
                )
                Text(text = if (filteredGames[index].genres != null) {
                    "Genres : " + filteredGames[index].genres!!.joinToString(separator = ", ") { it["name"] ?: "Inconnu" }
                } else {
                    "Genres : Non disponible"
                },
                    maxLines = 2,
                    color = Color(0, 109, 119),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(5.dp)
                )

            }
            Column {
                IconButton(
                    onClick = {
                        GererFavoris.toggleFavorite(filteredGames[index].id)
                    },
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.TwoTone.Star,
                        contentDescription = "Favori",
                        tint = if (isFavorite) Color(0, 109, 119) else Color(131, 197, 190)
                    )
                }
            }
        }
    }
}