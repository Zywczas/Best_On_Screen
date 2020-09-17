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
    fun checkIfIsInDb_observeTrue(){
        val returnedData = Flowable.just(Event(true))
        `when`(repo.checkIfMovieIsInDB(anyInt())).thenReturn(returnedData)

        viewModel.checkIfIsInDb(anyInt())
        val actual = LiveDataTestUtil.getValue(viewModel.isMovieInDbLD).getContentIfNotHandled()

        assertEquals(true, actual)
    }

    @Test
    fun checkIfIsInDb_observeFalse(){
        val returnedData = Flowable.just(Event(false))
        `when`(repo.checkIfMovieIsInDB(anyInt())).thenReturn(returnedData)

        viewModel.checkIfIsInDb(anyInt())
        val actual = LiveDataTestUtil.getValue(viewModel.isMovieInDbLD).getContentIfNotHandled()

        assertEquals(false, actual)
    }

    @Test
    fun addOrDeleteMovie_add_observeMessage(){
        val isInDb = false
        val expectedMessage = "some add outcome"
        val returnedData = Flowable.just(Event(expectedMessage))
        `when`(repo.addMovieToDB(movie)).thenReturn(returnedData)

        viewModel.addOrDeleteMovie(movie, isInDb)
        val actualMessage = LiveDataTestUtil.getValue(viewModel.messageLD).getContentIfNotHandled()

        verify(repo).addMovieToDB(movie)
        verifyNoMoreInteractions(repo)
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun addOrDeleteMovie_delete_observeMessage(){
        val isInDb = true
        val expectedMessage = "some delete outcome"
        val returnedData = Flowable.just(Event(expectedMessage))
        `when`(repo.deleteMovieFromDB(movie)).thenReturn(returnedData)

        viewModel.addOrDeleteMovie(movie, isInDb)
        val actualMessage = LiveDataTestUtil.getValue(viewModel.messageLD).getContentIfNotHandled()

        verify(repo).deleteMovieFromDB(movie)
        verifyNoMoreInteractions(repo)
        assertEquals(expectedMessage, actualMessage)
    }

}