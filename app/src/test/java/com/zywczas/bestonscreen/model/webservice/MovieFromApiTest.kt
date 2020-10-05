package com.zywczas.bestonscreen.model.webservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class MovieFromApiTest {

    @Nested
    inner class ConvertGenres {

        @Test
        fun returnSuccess() {
            val expected = "Genres: Action, Fantasy, Family, Romance, Western"
            val movieFromApi = MovieFromApi(null, null, listOf(28, 14, 10751, 10749, 37),
                null, null, null, null)

            movieFromApi.convertGenreIdsToDescription()

            assertEquals(expected, movieFromApi.genresDescription)
        }

        @Test
        fun wrongID_returnMissingInfo() {
            val expected = "Genres: missing info, Fantasy, missing info"
            val wrongId1 = 123456
            val wrongId2 = 0
            val movieFromApi = MovieFromApi(null, null, listOf(wrongId1, 14, wrongId2),
                null, null, null, null)

            movieFromApi.convertGenreIdsToDescription()

            assertEquals(expected, movieFromApi.genresDescription)
        }

        @Test
        fun otherThan1To5GenresInList_returnMissingInfo() {
            val expected = "Genres: missing information"
            val movieFromApi = MovieFromApi(null, null,
                listOf(12, 16, 80, 28, 14, 10751, 10749, 37), null, null,
                null, null
            )

            movieFromApi.convertGenreIdsToDescription()

            assertEquals(expected, movieFromApi.genresDescription)
        }

        @Test
        fun nullIds_returnNoInformation() {
            val expected = "Genres: no information"
            val movieFromApi = MovieFromApi(null, null, null,
            null, null, null, null)

            movieFromApi.convertGenreIdsToDescription()

            assertEquals(expected, movieFromApi.genresDescription)
        }

    }

}