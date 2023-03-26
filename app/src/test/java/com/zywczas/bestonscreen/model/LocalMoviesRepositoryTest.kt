package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.LocalMovie
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.repositories.LocalMoviesRepository
import com.zywczas.bestonscreen.util.TestUtil
import io.reactivex.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

internal class LocalMoviesRepositoryTest {

    private val movieDao = mock(MovieDao::class.java)
    private val tested = LocalMoviesRepository(movieDao)

    @Nested
    inner class GetMoviesFromDb {

        @Test
        fun returnMovieList() {
            val expectedMovies = TestUtil.moviesList1_2
            val moviesFromDB = TestUtil.moviesFromDb
            val returnedMoviesFromDb = Flowable.just(moviesFromDB)
            `when`(movieDao.getMovies()).thenReturn(returnedMoviesFromDb)

            val actualMovies = tested.getMoviesFromDB().blockingFirst()

            assertEquals(expectedMovies, actualMovies)
            verify(movieDao).getMovies()
            verifyNoMoreInteractions(movieDao)
        }

        @Test
        fun emptyDB_returnEmptyList() {
            val returnedMoviesFromDb = Flowable.just(emptyList<LocalMovie>())
            `when`(movieDao.getMovies()).thenReturn(returnedMoviesFromDb)

            val actual = tested.getMoviesFromDB().blockingFirst()

            assertEquals(emptyList<Movie>(), actual)
        }
    }

}
