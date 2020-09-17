package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.util.TestUtil
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

internal class DetailsRepositoryTest{

    private lateinit var detailsRepo : DetailsRepository

    @Mock
    private lateinit var movieDao: MovieDao
    private val movie = TestUtil.movie1
    private val movieFromDB = TestUtil.movieFromDB1

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        detailsRepo = DetailsRepository(movieDao)
    }

    @Test
    fun checkIfMovieIsInDB_returnTrue(){
        val movieId = 43
        val returnedData = Flowable.just(1)
        `when`(movieDao.getIdCount(movieId)).thenReturn(returnedData)

        val actual = detailsRepo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()

        verify(movieDao).getIdCount(movieId)
        verifyNoMoreInteractions(movieDao)
        assertEquals(true, actual)
    }

    @Test
    fun checkIfMovieIsInDB_returnFalse(){
        val movieId = 43
        val returnedData = Flowable.just(0)
        `when`(movieDao.getIdCount(movieId)).thenReturn(returnedData)

        val actual = detailsRepo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()

        assertEquals(false, actual)
    }

    @Test
    fun addMovieToDB_returnSuccess(){
        val returnedRowId = Single.just(1L)
        val expectedMessage = detailsRepo.addSuccess
        `when`(movieDao.insertMovie(movieFromDB)).thenReturn(returnedRowId)

        val actualMessage = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

        verify(movieDao).insertMovie(movieFromDB)
        verifyNoMoreInteractions(movieDao)
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun addMovieToDB_returnFailure(){
        val returnedRowId = Single.just(0L)
        val expectedMessage = detailsRepo.addError
        `when`(movieDao.insertMovie(movieFromDB)).thenReturn(returnedRowId)

        val actualMessage = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun addMovieToDB_throwException_returnFailure(){
        val returnedData = Single.error<Long>(Exception())
        val expectedMessage = detailsRepo.addError
        `when`(movieDao.insertMovie(movieFromDB)).thenReturn(returnedData)

        val actualMessage = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()

        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun deleteMovieFromDB_returnSuccess(){
        val numberOfRowsRemoved = Single.just(1)
        val expectedMessage = detailsRepo.deleteSuccess
        `when`(movieDao.deleteMovie(movieFromDB)).thenReturn(numberOfRowsRemoved)

        val actualMessage = detailsRepo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

        verify(movieDao).deleteMovie(movieFromDB)
        verifyNoMoreInteractions(movieDao)
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun deleteMovieFromDB_returnFailure(){
        val numberOfRowsRemoved = Single.just(0)
        val expectedMessage = detailsRepo.deleteError
        `when`(movieDao.deleteMovie(movieFromDB)).thenReturn(numberOfRowsRemoved)

        val actualMessage = detailsRepo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()

        assertEquals(expectedMessage, actualMessage)
    }


}