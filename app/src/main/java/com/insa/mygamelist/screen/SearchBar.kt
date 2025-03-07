package com.insa.mygamelist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(searchQuery: String, onSearchQueryChanged: (String) -> Unit) {
    TextField(
        value = searchQuery,
        textStyle = TextStyle(fontSize = 17.sp),
        onValueChange = { onSearchQueryChanged(it) },
        singleLine = true,
        placeholder = {
            Text(
                "Rechercher un jeu...",
                color = Color(0, 109, 119),
                fontSize = 17.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(Color(237, 246, 255)),
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Effacer la recherche"
                    )
                }
            }
        }
    )
}
