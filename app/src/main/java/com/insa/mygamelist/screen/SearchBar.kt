package com.insa.mygamelist.screen


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(237, 246, 255),
            unfocusedContainerColor = Color(237, 246, 255),
            disabledContainerColor = Color(237, 246, 255),
            errorContainerColor = Color(237, 246, 255),
            disabledPlaceholderColor = Color(50, 159, 169),
            errorPlaceholderColor = Color(50, 159, 169),
            focusedPlaceholderColor = Color(50, 159, 169),
            unfocusedPlaceholderColor = Color(50, 159, 169),
            focusedTextColor = Color(0, 109, 119),
            disabledTextColor = Color(0, 109, 119),
            unfocusedTextColor = Color(0, 109, 119),
            errorTextColor = Color(0, 109, 119),
            cursorColor = Color(0, 109, 119)
        ),
        placeholder = {
            Text(
                "Rechercher un jeu...",
                fontSize = 17.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),

        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Effacer la recherche",
                        tint = Color(0, 109, 119)
                    )
                }
            }
        }
    )
}
