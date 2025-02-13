package com.insa.mygamelist.screen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameList(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(colors = topAppBarColors(
            containerColor = Color(144,122,200),
            titleContentColor = Color.Black,
        ), title = { Text("My Games List") })
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(IGDB.games.size) { index ->
                Box(
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray)
                            .padding(5.dp)
                            .clickable { navController.navigate(GameInfor(IGDB.games[index].id)) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.padding(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = "https:${IGDB.covers.find { it.id == IGDB.games[index].cover }?.url}",
                                modifier = Modifier.padding(1.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentDescription = null
                            )
                        }
                        Column(modifier = Modifier.padding(2.dp)) {
                            Text(
                                text = IGDB.games[index].name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W600,
                                style = TextStyle(textDecoration = TextDecoration.Underline),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(5.dp)
                            )
                            Text(text = "Genres : " + IGDB.genres.filter { it.id in IGDB.games[index].genres }
                                .joinToString(separator = ", ") { it.name },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}