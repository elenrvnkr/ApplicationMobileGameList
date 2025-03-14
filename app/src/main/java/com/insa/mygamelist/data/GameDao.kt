package com.insa.mygamelist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//Database (jeu hors ligne)
@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<GameEntity>

    @Query("DELETE FROM games")
    suspend fun clearGames()
}
