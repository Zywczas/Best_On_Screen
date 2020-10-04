package com.zywczas.bestonscreen.viewmodels

import androidx.test.internal.util.ReflectionUtil
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import org.junit.jupiter.api.extension.ExtendWith
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.internal.util.reflection.FieldSetter

@ExtendWith(InstantExecutorExtension::class)
internal class ApiVMTest {

    private lateinit var viewModel : ApiVM
    //todo dac 2 testy nowe na getFirstMovies + internet check
    @Mock
    private lateinit var repo : ApiRepository
    @Mock
    private lateinit var network : NetworkCheck

    @BeforeEach
    private fun init() {
        MockitoAnnotations.initMocks(this)
        viewModel = ApiVM(repo, network)
    }

    private fun <Category> anyCategory(): Category = any()

    @Nested
    inner class GetNextMovies {

        @Test
        fun observeChange() {
            val category = Category.POPULAR
            val movies = TestUtil.movies
            val returnedMovies = Flowable.just(Resource.success(movies))
            `when`(repo.getApiMovies(anyCategory(), anyInt())).thenReturn(returnedMovies)
            `when`(network.isConnected()).thenReturn(true)

            viewModel.getNextMoviesIfConnected(category)
            val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            assertEquals(Resource.success(Pair(movies, category)), actual)
        }

        @Test
        fun error_observeError() {
            val category = Category.UPCOMING
            val message = "some error"
            val returnedError: Flowable<Resource<List<Movie>>> = Flowable.just(Resource.error(message, null))
            `when`(repo.getApiMovies(anyCategory(), anyInt())).thenReturn(returnedError)
            `when`(network.isConnected()).thenReturn(true)

            viewModel.getNextMoviesIfConnected(category)
            val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            verify(repo).getApiMovies(anyCategory(), anyInt())
            verifyNoMoreInteractions(repo)
            assertEquals(Resource.error(message, Pair(emptyList<Movie>(), category)), actual)
        }

        @Test
        fun `1st call OK, 2nd error, observe error and data`() {
            val category = Category.UPCOMING
            val message = "some error message from ApiRepository"
            val movies = TestUtil.movies
            val returnedMovies = Flowable.just(Resource.success(movies))
            `when`(repo.getApiMovies(category, 1)).thenReturn(returnedMovies)
            val returnedError: Flowable<Resource<List<Movie>>> = Flowable.just(Resource.error(message, null))
            `when`(repo.getApiMovies(category, 2)).thenReturn(returnedError)
            `when`(network.isConnected()).thenReturn(true)

            viewModel.getNextMoviesIfConnected(category)
            val needToPullFirstValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
            viewModel.getNextMoviesIfConnected(category)
            val actualSecondValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            verify(repo, atLeast(2)).getApiMovies(anyCategory(), anyInt())
            verifyNoMoreInteractions(repo)
            assertEquals(Resource.error(message, Pair(movies, category)), actualSecondValue)
        }

    }

}