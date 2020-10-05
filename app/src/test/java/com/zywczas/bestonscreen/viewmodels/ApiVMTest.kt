package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import org.junit.jupiter.api.extension.ExtendWith
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@ExtendWith(InstantExecutorExtension::class)
internal class ApiVMTest {

    private lateinit var viewModel : ApiVM
    //todo dac 2 testy nowe na getFirstMovies + internet check

    @MockK
    lateinit var repo : ApiRepository
    @MockK
    lateinit var network : NetworkCheck

    @BeforeEach
    private fun init() {
        MockKAnnotations.init(this)
        viewModel = ApiVM(repo, network)
    }

    @Nested
    inner class GetNextMovies {

        @Test
        fun observeChange() {
            val category = Category.POPULAR
            val movies = TestUtil.movies
            val returnedMovies = Flowable.just(Resource.success(movies))
            every { repo.getApiMovies(category, 1) } returns returnedMovies
            every { network.isConnected } returns true

            viewModel.getNextMoviesIfConnected(category)
            val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            assertEquals(Resource.success(Pair(movies, category)), actual)
        }

        @Test
        fun error_observeError() {
            val category = Category.UPCOMING
            val message = "some error"
            val returnedError: Flowable<Resource<List<Movie>>> = Flowable.just(Resource.error(message, null))
            every { repo.getApiMovies(category, 1) } returns returnedError
            every { network.isConnected } returns true

            viewModel.getNextMoviesIfConnected(category)
            val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            assertEquals(Resource.error(message, Pair(emptyList<Movie>(), category)), actual)
            verify (exactly = 1) { repo.getApiMovies(any(), any()) }
        }

        @Test
        fun `1st call OK, 2nd error, observe error and data`() {
            val category = Category.TOP_RATED
            val message = "some error message from ApiRepository"
            val movies = TestUtil.movies
            val pages = mutableListOf<Int>()
            val returnedMovies = Flowable.just(Resource.success(movies))
            val returnedError: Flowable<Resource<List<Movie>>> = Flowable.just(Resource.error(message, null))
            every { repo.getApiMovies(category, capture(pages)) } returns returnedMovies andThen returnedError
            every { network.isConnected } returns true

            viewModel.getNextMoviesIfConnected(category)
            val needToPullFirstValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
            viewModel.getNextMoviesIfConnected(category)
            val actualSecondValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            assertEquals(Resource.error(message, Pair(movies, category)), actualSecondValue)
            verify (exactly = 2) { repo.getApiMovies(category, any()) }
            assertEquals(listOf(1,2), pages)
        }
        //todo dac 2 testy nowe na getFirstMovies + internet check

    }

}