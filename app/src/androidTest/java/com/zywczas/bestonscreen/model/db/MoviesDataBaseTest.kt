package com.zywczas.bestonscreen.model.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.*

internal abstract class MoviesDataBaseTest {

    //system under test
    lateinit var dataBase: MoviesDataBase
    //todo spawdzic czy nie dac private
    @Before
    fun init(){
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDataBase::class.java
        ).build()
    }

    @After
    fun finish(){
        dataBase.close()
    }
}