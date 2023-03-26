package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.repositories.MovieDetailsRepository
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*

@ExtendWith(InstantExecutorExtension::class)
internal class MovieDetailsViewModelTest {

    private lateinit var viewModel: MovieDetailsViewModel
    private val repo = mock(MovieDetailsRepository::class.java)
    private val movie = TestUtil.movie1

    @BeforeEach
    private fun init() {
        viewModel = MovieDetailsViewModel(repo) //todo remove this initialization
    }

    @Nested
    inner class CheckIfIsInDb {

        @Test
        fun observeTrue() {
            val returnedTrue = Flowable.just(Event(true))
            `when`(repo.checkIfMovieIsInDB(anyInt())).thenReturn(returnedTrue)

            viewModel.checkIfIsInDb(777)
            val actual = LiveDataTestUtil.getValue(viewModel.isMovieInDb).getContentIfNotHandled()

            assertEquals(true, actual)
            verify(repo).checkIfMovieIsInDB(777)
            verifyNoMoreInteractions(repo)
        }

        @Test
        fun observeFalse() {
            val returnedFalse = Flowable.just(Event(false))
            `when`(repo.checkIfMovieIsInDB(anyInt())).thenReturn(returnedFalse)

            viewModel.checkIfIsInDb(777)
            val actual = LiveDataTestUtil.getValue(viewModel.isMovieInDb).getContentIfNotHandled()

            assertEquals(false, actual)
        }
    }

    @Nested
    inner class AddOrDeleteMovie {

        @Test
        fun add_observeMessage() {
            val expectedMessage = "some add outcome"
            val isInDb = false
            val returnedData = Flowable.just(Event(expectedMessage))
            `when`(repo.addMovieToDB(movie)).thenReturn(returnedData)

            viewModel.addOrDeleteMovie(movie, isInDb)
            val actualMessage = LiveDataTestUtil.getValue(viewModel.message).getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
            verify(repo).addMovieToDB(movie)
            verifyNoMoreInteractions(repo)
        }

        @Test
        fun delete_observeMessage() {
            val expectedMessage = "some delete outcome"
            val isInDb = true
            val returnedData = Flowable.just(Event(expectedMessage))
            `when`(repo.deleteMovieFromDB(movie)).thenReturn(returnedData)

            viewModel.addOrDeleteMovie(movie, isInDb)
            val actualMessage = LiveDataTestUtil.getValue(viewModel.message).getContentIfNotHandled()

            assertEquals(expectedMessage, actualMessage)
            verify(repo).deleteMovieFromDB(movie)
            verifyNoMoreInteractions(repo)
        }
    }
}
