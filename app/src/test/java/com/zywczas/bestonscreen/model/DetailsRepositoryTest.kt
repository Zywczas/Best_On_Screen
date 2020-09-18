package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.util.TestUtil
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

internal class DetailsRepositoryTest {

    private lateinit var detailsRepo: DetailsRepository

    @Mock
    private lateinit var movieDao: MovieDao

    private val movie = TestUtil.movie1

    @BeforeEach
    private fun init() {
        MockitoAnnotations.initMocks(this)
        detailsRepo = DetailsRepository(movieDao)
    }

    private fun <MovieFromDB> anyMovieFromDB() : MovieFromDB = any()

    @Test
    fun checkIfMovieIsInDB_returnTrue() {
        val movieId = 777
        val returnedIdCount = Flowable.just(1)
        `when`(movieDao.getIdCount(movieId)).thenReturn(returnedIdCount)

        val actual =
            detailsRepo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()

        verify(movieDao).getIdCount(movieId)
        verifyNoMoreInteractions(movieDao)
        assertEquals(true, actual)
    }

    @Test
    fun checkIfMovieIsInDB_returnFalse() {
        val movieId = 777
        val returnedIdCount = Flowable.just(0)
        `when`(movieDao.getIdCount(movieId)).thenReturn(returnedIdCount)

        val actual =
            detailsRepo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()

        assertEquals(false, actual)
    }

    @Test
    fun addMovieToDB_returnSuccess() {
        val expectedMessage = detailsRepo.addSuccess
        val returnedRowId = Single.just(1L)
        `when`(movieDao.insertMovie(anyMovieFromDB())).thenReturn(returnedRowId)

        val actualMessage = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

        verify(movieDao).insertMovie(anyMovieFromDB())
        verifyNoMoreInteractions(movieDao)
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun addMovieToDB_returnFailure() {
        val expectedMessage = detailsRepo.addError
        val returnedRowId = Single.just(0L)
        `when`(movieDao.insertMovie(anyMovieFromDB())).thenReturn(returnedRowId)

        val actualMessage = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun addMovieToDB_throwException_returnFailure() {
        val expectedMessage = detailsRepo.addError
        val returnedException = Single.error<Long>(Exception())
        `when`(movieDao.insertMovie(anyMovieFromDB())).thenReturn(returnedException)

        val actualMessage = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun deleteMovieFromDB_returnSuccess() {
        val expectedMessage = detailsRepo.deleteSuccess
        val numberOfRowsRemoved = Single.just(1)
        `when`(movieDao.deleteMovie(anyMovieFromDB())).thenReturn(numberOfRowsRemoved)

        val actualMessage =
            detailsRepo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

        verify(movieDao).deleteMovie(anyMovieFromDB())
        verifyNoMoreInteractions(movieDao)
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun deleteMovieFromDB_returnFailure() {
        val expectedMessage = detailsRepo.deleteError
        val numberOfRowsRemoved = Single.just(0)
        `when`(movieDao.deleteMovie(anyMovieFromDB())).thenReturn(numberOfRowsRemoved)

        val actualMessage =
            detailsRepo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

        assertEquals(expectedMessage, actualMessage)
    }


}