package com.zywczas.bestonscreen.model.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zywczas.bestonscreen.util.TestUtil
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

internal class MovieDaoTest : MoviesDataBaseTest() {

    //it has to have @JvmField annotation to 'make this rule public' - otherwise gives an error in Kotlin
    @Rule @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun insert(){
        //arrange
        val movieFromDb = TestUtil.movieFromDB1
        //act
        val rowId = dao.insertMovie(movieFromDb).blockingGet()
        val movieIdCount = dao.getIdCount(movieFromDb.id).blockingFirst()
        //assert
        assertEquals(1, movieIdCount)
    }

}