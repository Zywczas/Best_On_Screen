package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

internal class DetailsRepositoryTest{

    //system under test
    lateinit var detailsRepo : DetailsRepository

    @Mock
    lateinit var movieDao: MovieDao
    lateinit var movie: Movie
    lateinit var movieFromDB: MovieFromDB

    @BeforeEach
    private fun init(){
        MockitoAnnotations.openMocks(this)
        detailsRepo = DetailsRepository(movieDao)
        movie = TestUtil.movie1
        movieFromDB = TestUtil.movieFromDB1
    }

    @Test
    fun checkIfMovieIsInDB_returnTrue(){
        //arrange
        val movieId = 43
        val returnedData = Flowable.just(1)
        `when`(movieDao.getIdCount(movieId)).thenReturn(returnedData)
        //act
        val returnedValue = detailsRepo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()
        //assert
        verify(movieDao).getIdCount(movieId)
        verifyNoMoreInteractions(movieDao)
        assertEquals(Resource.success(true), returnedValue)
    }

    @Test
    fun checkIfMovieIsInDB_returnFalse(){
        //arrange
        val movieId = 43
        val returnedData = Flowable.just(0)
        `when`(movieDao.getIdCount(movieId)).thenReturn(returnedData)
        //act
        val returnedValue = detailsRepo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()
        //assert
        assertEquals(Resource.success(false), returnedValue)
    }

    @Test
    fun checkIfMovieIsInDB_throwException_returnError(){
        //arrange
        val movieId = 43
        val exception = Flowable.error<Int>(Exception())
        val errorMessage = detailsRepo.checkError
        `when`(movieDao.getIdCount(movieId)).thenReturn(exception)
        //act
        val returnedValue = detailsRepo.checkIfMovieIsInDB(movieId).blockingFirst().getContentIfNotHandled()
        //assert
        assertEquals(Resource.error(errorMessage, null), returnedValue)
    }

    @Test
    fun addMovieToDB_returnSuccess(){
        //arrange
        val returnedRowId = Single.just(1L)
        val expectedMessage = detailsRepo.addSuccess
        `when`(movieDao.insertMovie(movieFromDB)).thenReturn(returnedRowId)
        //act
        val returnedValue = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()
        //assert
        verify(movieDao).insertMovie(movieFromDB)
        verifyNoMoreInteractions(movieDao)
        assertEquals(expectedMessage, returnedValue)
    }

    @Test
    fun addMovieToDB_returnFailure(){
        //arrange
        val returnedRowId = Single.just(0L)
        val expectedMessage = detailsRepo.addError
        `when`(movieDao.insertMovie(movieFromDB)).thenReturn(returnedRowId)
        //act
        val returnedValue = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()
        //assert
        assertEquals(expectedMessage, returnedValue)
    }

    @Test
    fun addMovieToDB_throwException_returnFailure(){
        //arrange
        val returnedData = Single.error<Long>(Exception())
        val expectedMessage = detailsRepo.addError
        `when`(movieDao.insertMovie(movieFromDB)).thenReturn(returnedData)
        //act
        val returnedValue = detailsRepo.addMovieToDB(movie).blockingFirst().getContentIfNotHandled()
        //assert
        assertEquals(expectedMessage, returnedValue)
    }

    @Test
    fun deleteMovieFromDB_returnSuccess(){
        //arrange
        val numberOfRowsRemoved = Single.just(1)
        val expectedMessage = detailsRepo.deleteSuccess
        `when`(movieDao.deleteMovie(movieFromDB)).thenReturn(numberOfRowsRemoved)
        //act
        val returnedValue = detailsRepo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()
        //assert
        verify(movieDao).deleteMovie(movieFromDB)
        verifyNoMoreInteractions(movieDao)
        assertEquals(expectedMessage, returnedValue)
    }

    @Test
    fun deleteMovieFromDB_returnFailure(){
        //arrange
        val numberOfRowsRemoved = Single.just(0)
        val expectedMessage = detailsRepo.deleteError
        `when`(movieDao.deleteMovie(movieFromDB)).thenReturn(numberOfRowsRemoved)
        //act
        val returnedValue = detailsRepo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()
        //assert
        assertEquals(expectedMessage, returnedValue)
    }
//todo moze pozamieniac tutaj tez klasy na any
    @Test
    fun deleteMovieFromDB_throwException_returnFailure(){
        //arrange
        val returnedData = Single.error<Int>(Exception())
        val expectedMessage = detailsRepo.deleteError
        `when`(movieDao.deleteMovie(movieFromDB)).thenReturn(returnedData)
        //act
        val returnedValue = detailsRepo.deleteMovieFromDB(movie).blockingFirst().getContentIfNotHandled()
        //assert
        assertEquals(expectedMessage, returnedValue)
    }


}