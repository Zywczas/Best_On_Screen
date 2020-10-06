package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
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
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(InstantExecutorExtension::class)
internal class ApiVMTest {

    private lateinit var viewModel : ApiVM

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
    inner class GetFirstMovies {

        @Test
        fun observeChange(){
            val category = Category.POPULAR
            val movies = TestUtil.moviesListOf2
            val returnedMovies = Flowable.just(Resource.success(movies))
            every { repo.getApiMovies(category, 1) } returns returnedMovies
            every { network.isConnected } returns true

            viewModel.getFirstMovies(category)
            val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            assertEquals(Resource.success(Pair(movies, category)), actual)
        }

        @Test
        fun noConnection_observeError(){
            val message = "Problem with internet. Check your connection and try again."
            val category = Category.UPCOMING
            val movies = TestUtil.moviesListOf2
            val returnedMovies = Flowable.just(Resource.success(movies))
            every { repo.getApiMovies(category, 1) } returns returnedMovies
            every { network.isConnected } returns false

            viewModel.getFirstMovies(category)
            val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            assertEquals(Resource.error(message, Pair(emptyList<Movie>(), category)), actual)
        }

        @Test
        fun tryToGetAgain_observeNoAction(){
            val category = Category.TOP_RATED
            val movies1 = TestUtil.moviesListOf2
            val movies2 = listOf(TestUtil.movie2)
            val returnedMovies1 = Flowable.just(Resource.success(movies1))
            val returnedMovies2 = Flowable.just(Resource.success(movies2))
            every { repo.getApiMovies(category, any()) } returns returnedMovies1 andThen returnedMovies2
            every { network.isConnected } returns true

            viewModel.getFirstMovies(category)
            val firstValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
            viewModel.getFirstMovies(category)
            val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            assertEquals(Resource.success(Pair(movies1, category)), actual)
            verify (exactly = 1) { repo.getApiMovies(any(), any()) }
        }

    }

    @Nested
    inner class GetNextMovies {

        @Test
        fun observeChange() {
            val category = Category.POPULAR
            val movies = TestUtil.moviesListOf2
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
            val movies = TestUtil.moviesListOf2
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

        @Test
        fun changeCategory_observeNewCategory(){
            val category1 = Category.TOP_RATED
            val category2 = Category.UPCOMING
            val movies1 = TestUtil.moviesListOf2
            val movies2 = listOf(TestUtil.movie2)
            val pages = mutableListOf<Int>()
            val returnedMovies1 = Flowable.just(Resource.success(movies1))
            val returnedMovies2 = Flowable.just(Resource.success(movies2))
            every { repo.getApiMovies(any(), capture(pages)) } returns returnedMovies1 andThen returnedMovies2
            every { network.isConnected } returns true

            viewModel.getNextMoviesIfConnected(category1)
            val firstValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
            viewModel.getNextMoviesIfConnected(category2)
            val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

            assertEquals(Resource.success(Pair(movies2, category2)), actual)
            assertEquals(listOf(1,1), pages)
        }

    }

}