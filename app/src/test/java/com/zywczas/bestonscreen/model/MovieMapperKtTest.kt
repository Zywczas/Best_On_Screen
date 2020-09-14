package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.util.TestUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MovieMapperKtTest {

    @Test
    fun toMovie_movieFromApi(){
        //arrange
        val expectedValue = TestUtil.movie1
        val movieFromApi = TestUtil.movieFromApi1
        //act
        val returnedValue = toMovie(movieFromApi)
        //assert
        assertEquals(expectedValue, returnedValue)
    }

    @Test
    fun toMovie_movieFromDb(){
        //arrange
        val expectedValue = TestUtil.movie1
        val movieFromDb = TestUtil.movieFromDB1
        //act
        val returnedValue = toMovie(movieFromDb)
        //assert
        assertEquals(expectedValue, returnedValue)
    }

    @Test
    fun toMovieFromDb_movie(){
        //arrange
        val expectedValue = TestUtil.movieFromDB1
        val movie = TestUtil.movie1
        //act
        val returnedValue = toMovieFromDB(movie)
        //assert
        assertEquals(expectedValue, returnedValue)
    }
}