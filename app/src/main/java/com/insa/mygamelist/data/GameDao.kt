package com.insa.mygamelist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//Database (jeu hors ligne)
@Dao
interface GameDao {
    // mets liste de jeu dans la database
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Si on a le même id on remplace
    suspend fun insertGames(games: List<GameEntity>)

    // les récupère tous
    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<GameEntity>

    // les supprime tous
    @Query("DELETE FROM games")
    suspend fun clearGames()
}
