package com.zywczas.bestonscreen.model.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before

internal abstract class MoviesDataBaseTest {

    private lateinit var dataBase: MoviesDataBase
    protected lateinit var dao: MovieDao

    @Before
    fun init() {
        dataBase = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                MoviesDataBase::class.java
            )
            .allowMainThreadQueries()
            .build()
        dao = dataBase.getMovieDao()
    }

    @After
    fun finish() {
        dataBase.close()
    }

}
