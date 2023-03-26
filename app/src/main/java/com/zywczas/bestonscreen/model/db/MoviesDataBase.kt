package com.zywczas.bestonscreen.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalMovie::class], version = 1)
abstract class MoviesDataBase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

}
