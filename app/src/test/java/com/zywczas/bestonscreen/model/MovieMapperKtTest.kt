package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.util.TestUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

internal class MovieMapperKtTest {

    @Test
    fun toMovie_movieFromApi() {
        val expectedMovie = TestUtil.movie1
        val movieFromApi = TestUtil.movieFromApi1

        val actualMovie = toMovie(movieFromApi)

        assertEquals(expectedMovie, actualMovie)
    }

    @Test
    fun toMovie_movieFromDb() {
        val expectedMovie = TestUtil.movie1
        val movieFromDb = TestUtil.movieFromDB1

        val actualMovie = toMovie(movieFromDb)

        assertEquals(expectedMovie, actualMovie)
    }

    @Test
    fun toMovieFromDb_movie() {
        val expectedMovieFromDB = TestUtil.movieFromDB1
        val movie = TestUtil.movie1

        val actualMovieFromDB = toMovieFromDB(movie)

        assertEquals(expectedMovieFromDB, actualMovieFromDB)
    }
}