package com.zywczas.bestonscreen.model.webservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MovieFromApiTest {

    private lateinit var movieFromApi: MovieFromApi

    @BeforeEach
    private fun init(){
        movieFromApi = MovieFromApi()
    }

    @Test
    fun convertGenres_returnSuccess(){
        val expectedValue28 = "Action"
        val expectedValue14 = "Fantasy"
        val expectedValue10751 = "Family"
        val expectedValue10749 = "Romance"
        val expectedValue37 = "Western"
        movieFromApi.genreIds = listOf(28, 14, 10751, 10749, 37)

        movieFromApi.convertGenreIdsToVariables()

        assertEquals(expectedValue28, movieFromApi.genre1)
        assertEquals(expectedValue14, movieFromApi.genre2)
        assertEquals(expectedValue10751, movieFromApi.genre3)
        assertEquals(expectedValue10749, movieFromApi.genre4)
        assertEquals(expectedValue37, movieFromApi.genre5)
    }

    @Test
    fun convertGenres_wrongID_returnMissingInfo(){
        val wrongId = 0
        val expectedValue123456 = "missing info"
        val expectedValue14 = "Fantasy"
        val expectedValueOfWrongId = "missing info"
        movieFromApi.genreIds = listOf(123456, 14, wrongId)

        movieFromApi.convertGenreIdsToVariables()

        assertEquals(expectedValue123456, movieFromApi.genre1)
        assertEquals(expectedValue14, movieFromApi.genre2)
        assertEquals(expectedValueOfWrongId, movieFromApi.genre3)
        assertEquals("", movieFromApi.genre4)
        assertEquals("", movieFromApi.genre5)
    }

    @Test
    fun convertGenres_moreThan5GenresInList_returnSuccess_5genres(){
        val expectedValue12 = "Adventure"
        val expectedValue16 = "Animation"
        val expectedValue80 = "Crime"
        val expectedValue28 = "Action"
        val expectedValue14 = "Fantasy"
        movieFromApi.genreIds = listOf(12, 16, 80 ,28, 14, 10751, 10749, 37)

        movieFromApi.convertGenreIdsToVariables()

        assertEquals(expectedValue12, movieFromApi.genre1)
        assertEquals(expectedValue16, movieFromApi.genre2)
        assertEquals(expectedValue80, movieFromApi.genre3)
        assertEquals(expectedValue28, movieFromApi.genre4)
        assertEquals(expectedValue14, movieFromApi.genre5)
    }

    @Test
    fun convertGenres_noIds_returnEmptyGenres(){
        movieFromApi.genreIds = null

        movieFromApi.convertGenreIdsToVariables()

        assertEquals(0, movieFromApi.assignedGenresAmount)
        assertEquals("", movieFromApi.genre1)
        assertEquals("", movieFromApi.genre2)
        assertEquals("", movieFromApi.genre3)
        assertEquals("", movieFromApi.genre4)
        assertEquals("", movieFromApi.genre5)
    }

}