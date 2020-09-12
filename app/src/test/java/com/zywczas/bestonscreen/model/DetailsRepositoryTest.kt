package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.Flowable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

internal class DetailsRepositoryTest{

    //system under test
    lateinit var detailsRepo : DetailsRepository

    @Mock
    lateinit var movieDao: MovieDao

    @BeforeEach
    private fun init(){
        MockitoAnnotations.openMocks(this)
        detailsRepo = DetailsRepository(movieDao)
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


    /*
    check if movie is in db, return error
     */
}