package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.Category.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.repositories.NetworkMoviesRepository
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class NetworkMoviesViewModelTest {

    private val repo = mockk<NetworkMoviesRepository>()
    private val network = mockk<NetworkCheck>()
    private val tested = NetworkMoviesViewModel(repo, network)

    private val movies = TestUtil.moviesList1_2

    @BeforeEach
    private fun setup() {
        every { network.isConnected } returns true
        every { repo.getApiMovies(any(), any()) } returns Flowable.just(Resource.success(movies))
    }

    @Nested
    inner class GetFirstMovies {

        @Test
        fun observeChange() {
            tested.getFirstMovies(POPULAR)
            val actual = LiveDataTestUtil.getValue(tested.moviesAndCategory)

            assertEquals(Resource.success(Pair(movies, POPULAR)), actual)
        }

        @Test
        fun noConnection_observeError() {
            every { network.isConnected } returns false

            tested.getFirstMovies(UPCOMING)
            val liveDataValue = LiveDataTestUtil.getValue(tested.moviesAndCategory)
            val actualMessage = liveDataValue.message?.getContentIfNotHandled()
            val actualData = liveDataValue.data

            assertEquals("Problem with internet. Check your connection and try again.", actualMessage)
            assertEquals(Pair(emptyList<Movie>(), UPCOMING), actualData)
        }

        @Test
        fun tryToGetAgain_observeNoAction() {
            tested.getFirstMovies(TOP_RATED)
            val firstValue = LiveDataTestUtil.getValue(tested.moviesAndCategory)
            tested.getFirstMovies(TOP_RATED)
            val actualValue = LiveDataTestUtil.getValue(tested.moviesAndCategory)

            assertEquals(Resource.success(Pair(movies, TOP_RATED)), actualValue)
            verify(exactly = 1) { repo.getApiMovies(TOP_RATED, 1) }
        }

    }

    @Nested
    inner class GetNextMovies {

        @Test
        fun observeChange() {
            tested.getNextMoviesIfConnected(POPULAR)
            val actual = LiveDataTestUtil.getValue(tested.moviesAndCategory)

            assertEquals(Resource.success(Pair(movies, POPULAR)), actual)
            verify(exactly = 1) { repo.getApiMovies(POPULAR, 1) }
        }

        @Test
        fun error_observeError() {
            val category = UPCOMING
            val expectedMessage = "some error"
            val returnedError: Flowable<Resource<List<Movie>>> = Flowable.just(Resource.error(expectedMessage, null))
            every { repo.getApiMovies(category, 1) } returns returnedError

            tested.getNextMoviesIfConnected(category)
            val liveDataValue = LiveDataTestUtil.getValue(tested.moviesAndCategory)
            val actualMessage = liveDataValue.message?.getContentIfNotHandled()
            val actualData = liveDataValue.data

            assertEquals(expectedMessage, actualMessage)
            assertEquals(Pair(emptyList<Movie>(), category), actualData)
            verify(exactly = 1) { repo.getApiMovies(category, 1) }
        }

        @Test
        fun `1st call OK, 2nd call error, observe error message and previous data`() {
            val category = TOP_RATED
            val expectedMessage = "some error message from ApiRepository"
            val pages = mutableListOf<Int>()
            val returnedMovies = Flowable.just(Resource.success(movies))
            val returnedError: Flowable<Resource<List<Movie>>> = Flowable.just(Resource.error(expectedMessage, null))
            every { repo.getApiMovies(category, capture(pages)) } returns returnedMovies andThen returnedError

            tested.getNextMoviesIfConnected(category)
            val needToPullFirstValue = LiveDataTestUtil.getValue(tested.moviesAndCategory)
            tested.getNextMoviesIfConnected(category)
            val secondValue = LiveDataTestUtil.getValue(tested.moviesAndCategory)
            val actualMessage = secondValue.message?.getContentIfNotHandled()
            val actualData = secondValue.data

            assertEquals(expectedMessage, actualMessage)
            assertEquals(Pair(movies, category), actualData)
            verify(exactly = 2) { repo.getApiMovies(category, any()) }
            assertEquals(listOf(1, 2), pages)
        }

        @Test
        fun changeCategory_observeNewCategory() {
            val category1 = TOP_RATED
            val category2 = UPCOMING
            val movies1 = TestUtil.moviesList1_2
            val movies2 = TestUtil.moviesList1_5
            val pages = mutableListOf<Int>()
            val returnedMovies1 = Flowable.just(Resource.success(movies1))
            val returnedMovies2 = Flowable.just(Resource.success(movies2))
            every { repo.getApiMovies(any(), capture(pages)) } returns returnedMovies1 andThen returnedMovies2

            tested.getNextMoviesIfConnected(category1)
            val firstValue = LiveDataTestUtil.getValue(tested.moviesAndCategory)
            tested.getNextMoviesIfConnected(category2)
            val actualValue = LiveDataTestUtil.getValue(tested.moviesAndCategory)

            assertEquals(Resource.success(Pair(movies2, category2)), actualValue)
            assertEquals(listOf(1, 1), pages)
        }
    }
}
