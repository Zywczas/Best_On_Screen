package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.repositories.NetworkMoviesRepository
import com.zywczas.bestonscreen.model.webservice.MovieResponse
import com.zywczas.bestonscreen.model.webservice.NetworkMovieService
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*


internal class NetworkMoviesRepositoryTest {

    private val apiService = mock(NetworkMovieService::class.java)
    private val tested = NetworkMoviesRepository(apiService)

    @Nested
    inner class GetApiMoviesReturnMovies {

        @Test
        fun categoryPopular() {
            val expectedMovies = TestUtil.moviesList1_2
            val apiResponse = TestUtil.apiResponse
            val returnedApiResponse = Single.just(apiResponse)
            `when`(apiService.getPopularMovies(anyString(), anyInt())).thenReturn(returnedApiResponse)

            val actual = tested.getApiMovies(MovieCategory.POPULAR, 4).blockingFirst()

            assertEquals(Resource.success(expectedMovies), actual)
            verify(apiService).getPopularMovies(anyString(), anyInt())
            verifyNoMoreInteractions(apiService)
        }

        @Test
        fun categoryTopRated() {
            val expectedMovies = TestUtil.moviesList1_2
            val apiResponse = TestUtil.apiResponse
            val returnedApiResponse = Single.just(apiResponse)
            `when`(apiService.getTopRatedMovies(anyString(), anyInt())).thenReturn(returnedApiResponse)

            val actual = tested.getApiMovies(MovieCategory.TOP_RATED, 6).blockingFirst()

            assertEquals(Resource.success(expectedMovies), actual)
            verify(apiService).getTopRatedMovies(anyString(), anyInt())
            verifyNoMoreInteractions(apiService)
        }

        @Test
        fun categoryUpcoming() {
            val expectedMovies = TestUtil.moviesList1_2
            val apiResponse = TestUtil.apiResponse
            val returnedApiResponse = Single.just(apiResponse)
            `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedApiResponse)

            val actual = tested.getApiMovies(MovieCategory.UPCOMING, 1).blockingFirst()

            assertEquals(Resource.success(expectedMovies), actual)
            verify(apiService).getUpcomingMovies(anyString(), anyInt())
            verifyNoMoreInteractions(apiService)
        }
    }

    @Nested
    inner class GetApiMoviesReturnError {

        @Test
        fun noMoviesReturned() {
            val returnedEmptyApiResponse = Single.just(MovieResponse())
            `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedEmptyApiResponse)

            val repoResponse = tested.getApiMovies(MovieCategory.UPCOMING, 1).blockingFirst()
            val actualMessage = repoResponse.message?.getContentIfNotHandled()
            val actualData = repoResponse.data

            assertEquals("No more pages in this category.", actualMessage)
            assertEquals(null, actualData)
        }

        @Test
        fun emptyListReturned() {
            val returnedEmptyData = Single.just(MovieResponse(emptyList()))
            `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedEmptyData)

            val repoResponse = tested.getApiMovies(MovieCategory.UPCOMING, 1).blockingFirst()
            val actualMessage = repoResponse.message?.getContentIfNotHandled()
            val actualData = repoResponse.data

            assertEquals("No more pages in this category.", actualMessage)
            assertEquals(null, actualData)
        }

        @Test
        fun noMorePagesException() {
            val noMorePagesResponseStatus = "HTTP 422"
            `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
                .thenReturn(Single.error(Exception(noMorePagesResponseStatus)))

            val repoResponse = tested.getApiMovies(MovieCategory.UPCOMING, 5).blockingFirst()
            val actualMessage = repoResponse.message?.getContentIfNotHandled()
            val actualData = repoResponse.data

            assertEquals("No more pages in this category.", actualMessage)
            assertEquals(null, actualData)
        }

        @Test
        fun invalidApiKeyException() {
            val invalidApiKeyResponseStatus = "HTTP 401"
            `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
                .thenReturn(Single.error(Exception(invalidApiKeyResponseStatus)))

            val repoResponse = tested.getApiMovies(MovieCategory.UPCOMING, 5).blockingFirst()
            val actualMessage = repoResponse.message?.getContentIfNotHandled()
            val actualData = repoResponse.data

            assertEquals("Invalid API key. Contact technical support.", actualMessage)
            assertEquals(null, actualData)
        }

        @Test
        fun otherException() {
            `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
                .thenReturn(Single.error(Exception()))

            val repoResponse = tested.getApiMovies(MovieCategory.UPCOMING, 5).blockingFirst()
            val actualMessage = repoResponse.message?.getContentIfNotHandled()
            val actualData = repoResponse.data

            assertEquals("Problem with downloading movies. Check connection and try again.", actualMessage)
            assertEquals(null, actualData)
        }
    }
}
