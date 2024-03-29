package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.repositories.MovieDetailsRepository
import com.zywczas.bestonscreen.util.TestUtil
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

internal class MovieDetailsRepositoryTest {

    private val movieDao = mock(MovieDao::class.java)
    private val tested = MovieDetailsRepository(movieDao)
    private val movie = TestUtil.movie1

    private fun <MovieFromDB> anyMovieFromDB() : MovieFromDB = any()

    @Nested
    inner class CheckIfMovieIsInDb {

        @Test
        fun returnTrue() {
            val movieId = 777
            val returnedIdCount = Flowable.just(1)
            `when`(movieDao.getIdCount(movieId)).thenReturn(returnedIdCount)

            val actual =
                tested.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()

            assertEquals(true, actual)
            verify(movieDao).getIdCount(movieId)
            verifyNoMoreInteractions(movieDao)
        }

        @Test
        fun returnFalse() {
            val movieId = 777
            val returnedIdCount = Flowable.just(0)
            `when`(movieDao.getIdCount(movieId)).thenReturn(returnedIdCount)

            val actual =
                tested.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()

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

            val actualMessage = tested.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
            verify(movieDao).insertMovie(anyMovieFromDB())
            verifyNoMoreInteractions(movieDao)
        }

        @Test
        fun getError_returnFailure() {
            val expectedMessage = "Cannot add the movie. Close the app. Try again."
            val returnedRowId = Single.just(0L)
            `when`(movieDao.insertMovie(anyMovieFromDB())).thenReturn(returnedRowId)

            val actualMessage = tested.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
        }

        @Test
        fun throwException_returnFailure() {
            val expectedMessage = "Cannot add the movie. Close the app. Try again."
            val returnedException = Single.error<Long>(Exception())
            `when`(movieDao.insertMovie(anyMovieFromDB())).thenReturn(returnedException)

            val actualMessage = tested.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

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
                tested.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
            verify(movieDao).deleteMovie(anyMovieFromDB())
            verifyNoMoreInteractions(movieDao)
        }

        @Test
        fun returnFailure() {
            val expectedMessage = "Cannot remove the movie. Close the app. Try again."
            val numberOfRowsRemoved = Single.just(0)
            `when`(movieDao.deleteMovie(anyMovieFromDB())).thenReturn(numberOfRowsRemoved)

            val actualMessage =
                tested.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
        }

        @Test
        fun unexpectedException_returnFailure(){
            val expectedMessage = "Cannot remove the movie. Close the app. Try again."
            val returnedError = Single.error<Int>(Exception())
            `when`(movieDao.deleteMovie(anyMovieFromDB())).thenReturn(returnedError)

            val actualMessage =
                tested.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
        }
    }
}
