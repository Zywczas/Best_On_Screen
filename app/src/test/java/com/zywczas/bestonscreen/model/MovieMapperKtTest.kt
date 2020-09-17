package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.util.TestUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MovieMapperKtTest {

    @Test
    fun toMovie_movieFromApi(){
        val expectedValue = TestUtil.movie1
        val movieFromApi = TestUtil.movieFromApi1

        val returnedValue = toMovie(movieFromApi)

        assertEquals(expectedValue, returnedValue)
    }

    @Test
    fun toMovie_movieFromDb(){
        val expectedValue = TestUtil.movie1
        val movieFromDb = TestUtil.movieFromDB1

        val returnedValue = toMovie(movieFromDb)

        assertEquals(expectedValue, returnedValue)
    }

    @Test
    fun toMovieFromDb_movie(){
        val expectedValue = TestUtil.movieFromDB1
        val movie = TestUtil.movie1

        val returnedValue = toMovieFromDB(movie)

        assertEquals(expectedValue, returnedValue)
    }
}