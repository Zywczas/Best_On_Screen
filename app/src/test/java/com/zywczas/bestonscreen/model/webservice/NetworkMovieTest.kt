package com.zywczas.bestonscreen.model.webservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class NetworkMovieTest {

    @Nested
    inner class ConvertGenres {

        @Test
        fun returnSuccess() {
            val expected = "Genres: Action, Fantasy, Family, Romance, Western"
            val movie = NetworkMovie(null, null, listOf(28, 14, 10751, 10749, 37), null, null, null, null)

            val actual = movie.getGenresDescription()

            assertEquals(expected, actual)
        }

        @Test
        fun wrongID_returnMissingInfo() {
            val expected = "Genres: unknown, Fantasy, unknown"
            val wrongId1 = 123456
            val wrongId2 = 0
            val movie = NetworkMovie(null, null, listOf(wrongId1, 14, wrongId2), null, null, null, null)

            val actual = movie.getGenresDescription()

            assertEquals(expected, actual)
        }

        @Test
        fun nullIds_returnNoInformation() {
            val expected = "Genres: no information"
            val movie = NetworkMovie(null, null, null, null, null, null, null)

            val actual = movie.getGenresDescription()

            assertEquals(expected, actual)
        }

        @Test
        fun emptyIdsList_returnNoInformation() {
            val expected = "Genres: no information"
            val movie = NetworkMovie(null, null, emptyList(), null, null, null, null)

            val actual = movie.getGenresDescription()

            assertEquals(expected, actual)
        }

        @Test
        fun moreThan1GenreInList_returnSuccess() {
            val expected = "Genres: Adventure, Animation"
            val movie = NetworkMovie(null, null, listOf(12, 16), null, null, null, null)

            val actual = movie.getGenresDescription()

            assertEquals(expected, actual)
        }
    }
}
