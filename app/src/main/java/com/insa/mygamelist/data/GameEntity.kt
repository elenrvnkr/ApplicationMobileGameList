package com.insa.mygamelist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val coverUrl: String?,
    val releaseDate: Long?,
    val genres: String?,
    val platforms: String?,
    val summary: String?,
    val rating: String?
)
