package com.insa.mygamelist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//permet de garder jeu hors ligne
@Database(entities = [GameEntity::class], version = 2) //version à changer dès que je change database
@TypeConverters
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) { //synchronisation pour éviter de doubler la database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).fallbackToDestructiveMigration() // supprime la database si la version change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

