package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.util.TestUtil
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.*

internal class DetailsRepositoryTest {

    private val movieDao = mock(MovieDao::class.java)
    private val repo = DetailsRepository(movieDao)

    private val movie = TestUtil.movie1

    @BeforeEach
    private fun init() {
        reset(movieDao)
    }

    private fun <MovieFromDB> anyMovieFromDB() : MovieFromDB = any()

    @Nested
    inner class CheckIfMovieIsInDb {

        @Test
        fun returnTrue() {
            val movieId = 777
            val returnedIdCount = Flowable.just(1)
            `when`(movieDao.getIdCount(movieId)).thenReturn(returnedIdCount)

            val actual =
                repo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()

            verify(movieDao).getIdCount(movieId)
            verifyNoMoreInteractions(movieDao)
            assertEquals(true, actual)
        }

        @Test
        fun returnFalse() {
            val movieId = 777
            val returnedIdCount = Flowable.just(0)
            `when`(movieDao.getIdCount(movieId)).thenReturn(returnedIdCount)

            val actual =
                repo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()

            assertEquals(false, actual)
        }

    }

    @Nested
    inner class AddMovieToDb {

        @Test
        fun returnSuccess() {
            val expectedMessage = "Movie added to your list."
            val returnedRowId = Single.just(1L)
            `when`(movieDao.insertMovie(anyMovieFromDB())).thenReturn(returnedRowId)

            val actualMessage = repo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

            verify(movieDao).insertMovie(anyMovieFromDB())
            verifyNoMoreInteractions(movieDao)
            assertEquals(expectedMessage, actualMessage)
        }

        @Test
        fun getError_returnFailure() {
            val expectedMessage = "Cannot add the movie. Close the app. Try again."
            val returnedRowId = Single.just(0L)
            `when`(movieDao.insertMovie(anyMovieFromDB())).thenReturn(returnedRowId)

            val actualMessage = repo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
        }

        @Test
        fun throwException_returnFailure() {
            val expectedMessage = "Cannot add the movie. Close the app. Try again."
            val returnedException = Single.error<Long>(Exception())
            `when`(movieDao.insertMovie(anyMovieFromDB())).thenReturn(returnedException)

            val actualMessage = repo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
        }

    }

    @Nested
    inner class DeleteMovieFromDb {

        @Test
        fun returnSuccess() {
            val expectedMessage = "Movie removed from your list."
            val numberOfRowsRemoved = Single.just(1)
            `when`(movieDao.deleteMovie(anyMovieFromDB())).thenReturn(numberOfRowsRemoved)

            val actualMessage =
                repo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

            verify(movieDao).deleteMovie(anyMovieFromDB())
            verifyNoMoreInteractions(movieDao)
            assertEquals(expectedMessage, actualMessage)
        }

        @Test
        fun returnFailure() {
            val expectedMessage = "Cannot remove the movie. Close the app. Try again."
            val numberOfRowsRemoved = Single.just(0)
            `when`(movieDao.deleteMovie(anyMovieFromDB())).thenReturn(numberOfRowsRemoved)

            val actualMessage =
                repo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
        }

    }

}