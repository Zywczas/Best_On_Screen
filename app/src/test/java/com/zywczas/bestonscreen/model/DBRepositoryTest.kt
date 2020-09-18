package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.util.TestUtil
import io.reactivex.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


internal class DBRepositoryTest {

    private lateinit var dbRepo: DBRepository

    @Mock
    private lateinit var movieDao: MovieDao

    @BeforeEach
    private fun init() {
        MockitoAnnotations.initMocks(this)
        dbRepo = DBRepository(movieDao)
    }

    @Nested
    inner class GetMoviesFromDb {

        @Test
        fun returnMovieList() {
            val expectedMovies = TestUtil.movies
            val moviesFromDB = TestUtil.moviesFromDb
            val returnedMoviesFromDb = Flowable.just(moviesFromDB)
            `when`(movieDao.getMovies()).thenReturn(returnedMoviesFromDb)

            val actualMovies = dbRepo.getMoviesFromDB().blockingFirst()

            verify(movieDao).getMovies()
            verifyNoMoreInteractions(movieDao)
            assertEquals(expectedMovies, actualMovies)

        }

        @Test
        fun emptyDB_returnEmptyList() {
            val returnedMoviesFromDb = Flowable.just(emptyList<MovieFromDB>())
            `when`(movieDao.getMovies()).thenReturn(returnedMoviesFromDb)

            val actual = dbRepo.getMoviesFromDB().blockingFirst()

            assertEquals(emptyList<Movie>(), actual)
        }

    }


}