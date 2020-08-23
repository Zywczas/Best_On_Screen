package com.zywczas.bestonscreen.model.webservice

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class MovieFromApiTest {

    //system under test
    lateinit var movieFromApi: MovieFromApi

    @BeforeEach
    private fun init(){
        movieFromApi = MovieFromApi()
    }

    @Test
    fun convertGenres_returnSuccess(){
        //arrange
        val expectedValue28 = "Action"
        val expectedValue14 = "Fantasy"
        val expectedValue10751 = "Family"
        val expectedValue10749 = "Romance"
        val expectedValue37 = "Western"
        val ids = listOf(28, 14, 10751, 10749, 37)
        //act
        movieFromApi.convertGenreIdsToVariables(ids)
        //assert
        assertEquals(expectedValue28, movieFromApi.genre1)
        assertEquals(expectedValue14, movieFromApi.genre2)
        assertEquals(expectedValue10751, movieFromApi.genre3)
        assertEquals(expectedValue10749, movieFromApi.genre4)
        assertEquals(expectedValue37, movieFromApi.genre5)
    }

    @Test
    fun convertGenres_wrongID_returnMissingInfo(){
        //arrange
        val expectedValue123456 = "missing info"
        val expectedValue14 = "Fantasy"
        val expectedValue0 = "missing info"
        val ids = listOf(123456, 14, 0)
        //act
        movieFromApi.convertGenreIdsToVariables(ids)
        //assert
        assertEquals(expectedValue123456, movieFromApi.genre1)
        assertEquals(expectedValue14, movieFromApi.genre2)
        assertEquals(expectedValue0, movieFromApi.genre3)
        assertEquals("", movieFromApi.genre4)
        assertEquals("", movieFromApi.genre5)
    }

    @Test
    fun convertGenres_moreThan5GenresInList_returnSuccess(){
        //arrange
        val expectedValue12 = "Adventure"
        val expectedValue16 = "Animation"
        val expectedValue80 = "Crime"
        val expectedValue28 = "Action"
        val expectedValue14 = "Fantasy"
        val ids = listOf(12, 16, 80 ,28, 14, 10751, 10749, 37)
        //act
        movieFromApi.convertGenreIdsToVariables(ids)
        //assert
        assertEquals(expectedValue12, movieFromApi.genre1)
        assertEquals(expectedValue16, movieFromApi.genre2)
        assertEquals(expectedValue80, movieFromApi.genre3)
        assertEquals(expectedValue28, movieFromApi.genre4)
        assertEquals(expectedValue14, movieFromApi.genre5)
    }

}