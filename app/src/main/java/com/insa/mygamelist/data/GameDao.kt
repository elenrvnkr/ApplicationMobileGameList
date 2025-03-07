package com.insa.mygamelist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromGenreList(genres: List<Map<String, String>>?): String {
        return genres?.joinToString(separator = ";") { it["name"] ?: "" } ?: ""
    }

    @TypeConverter
    fun toGenreList(data: String): List<Map<String, String>> {
        return if (data.isNotEmpty()) data.split(";").map { mapOf("name" to it) } else emptyList()
    }

    @TypeConverter
    fun fromPlatformList(platforms: List<Platform>?): String {
        return platforms?.joinToString(separator = ";") { it.name ?: "" } ?: ""
    }

    @TypeConverter
    fun toPlatformList(data: String): List<Platform> {
        return if (data.isNotEmpty()) data.split(";").map { Platform(null, it, null) } else emptyList()
    }

    @TypeConverter
    fun fromCoverMap(cover: Map<String, String>?): String {
        return cover?.get("image_id") ?: ""
    }

    @TypeConverter
    fun toCoverMap(data: String): Map<String, String> {
        return if (data.isNotEmpty()) mapOf("image_id" to data) else emptyMap()
    }
}



@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<GameEntity>

    @Query("DELETE FROM games")
    suspend fun clearGames()
}
