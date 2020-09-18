package com.zywczas.bestonscreen.model.webservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

internal class MovieFromApiTest {

    private lateinit var movieFromApi: MovieFromApi

    @BeforeEach
    private fun init() {
        movieFromApi = MovieFromApi()
    }

    @Nested
    inner class ConvertGenres {

        @Test
        fun returnSuccess() {
            movieFromApi.genreIds = listOf(28, 14, 10751, 10749, 37)

            movieFromApi.convertGenreIdsToVariables()

            assertEquals(5, movieFromApi.assignedGenresAmount)
            assertEquals("Action", movieFromApi.genre1)
            assertEquals("Fantasy", movieFromApi.genre2)
            assertEquals("Family", movieFromApi.genre3)
            assertEquals("Romance", movieFromApi.genre4)
            assertEquals("Western", movieFromApi.genre5)
        }

        @Test
        fun wrongID_returnMissingInfo() {
            val wrongId1 = 123456
            val wrongId2 = 0
            movieFromApi.genreIds = listOf(wrongId1, 14, wrongId2)

            movieFromApi.convertGenreIdsToVariables()

            assertEquals(3, movieFromApi.assignedGenresAmount)
            assertEquals("missing info", movieFromApi.genre1)
            assertEquals("Fantasy", movieFromApi.genre2)
            assertEquals("missing info", movieFromApi.genre3)
            assertEquals("", movieFromApi.genre4)
            assertEquals("", movieFromApi.genre5)
        }

        @Test
        fun moreThan5GenresInList_return5genres() {
            movieFromApi.genreIds = listOf(12, 16, 80, 28, 14, 10751, 10749, 37)

            movieFromApi.convertGenreIdsToVariables()

            assertEquals(5, movieFromApi.assignedGenresAmount)
            assertEquals("Adventure", movieFromApi.genre1)
            assertEquals("Animation", movieFromApi.genre2)
            assertEquals("Crime", movieFromApi.genre3)
            assertEquals("Action", movieFromApi.genre4)
            assertEquals("Fantasy", movieFromApi.genre5)
        }

        @Test
        fun noIds_returnEmptyGenres() {
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

}