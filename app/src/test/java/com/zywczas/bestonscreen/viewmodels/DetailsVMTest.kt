package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.DetailsRepository

import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
internal class DetailsVMTest {

    //system under test
    private lateinit var viewModel : DetailsVM

    @Mock
    private lateinit var repo : DetailsRepository
    private val movie = TestUtil.movie1

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        viewModel = DetailsVM(repo)
    }

    @Test
    fun checkIfIsInDb_returnTrue(){
        //arrange
        val returnedData = Flowable.just(Event(true))
        `when`(repo.checkIfMovieIsInDB(anyInt())).thenReturn(returnedData)
        //act
        viewModel.checkIfIsInDb(anyInt())
        val returnedValue = LiveDataTestUtil.getValue(viewModel.isMovieInDbLD).getContentIfNotHandled()
        //assert
        assertEquals(true, returnedValue)
    }

    @Test
    fun checkIfIsInDb_returnFalse(){
        //arrange
        val returnedData = Flowable.just(Event(false))
        `when`(repo.checkIfMovieIsInDB(anyInt())).thenReturn(returnedData)
        //act
        viewModel.checkIfIsInDb(anyInt())
        val returnedValue = LiveDataTestUtil.getValue(viewModel.isMovieInDbLD).getContentIfNotHandled()
        //assert
        assertEquals(false, returnedValue)
    }

    @Test
    fun addMovie_returnMessage(){
        //arrange
        val isInDb = false
        val message = "some add outcome"
        val returnedData = Flowable.just(Event(message))
        `when`(repo.addMovieToDB(movie)).thenReturn(returnedData)
        //act
        viewModel.addOrDeleteMovie(movie, isInDb)
        val returnedValue = LiveDataTestUtil.getValue(viewModel.messageLD).getContentIfNotHandled()
        //assert
        verify(repo).addMovieToDB(movie)
        verifyNoMoreInteractions(repo)
        assertEquals(message, returnedValue)
    }

    @Test
    fun deleteMovie_returnMessage(){
        //arrange
        val isInDb = true
        val message = "some delete outcome"
        val returnedData = Flowable.just(Event(message))
        `when`(repo.deleteMovieFromDB(movie)).thenReturn(returnedData)
        //act
        viewModel.addOrDeleteMovie(movie, isInDb)
        val returnedValue = LiveDataTestUtil.getValue(viewModel.messageLD).getContentIfNotHandled()
        //assert
        verify(repo).deleteMovieFromDB(movie)
        verifyNoMoreInteractions(repo)
        assertEquals(message, returnedValue)
    }

}