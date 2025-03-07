package com.insa.mygamelist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val coverUrl: String?, // Stocke uniquement l'URL de l'image
    val releaseDate: Long?,
    val genres: String?, // Stocke sous forme de String (ex: "Action,RPG")
    val platforms: String?, // Stocke sous forme de String (ex: "PC,PS5,Xbox")
    val summary: String?,
    val rating: String?
)
