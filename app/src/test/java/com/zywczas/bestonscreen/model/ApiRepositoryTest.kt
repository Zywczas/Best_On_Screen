package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.webservice.ApiResponse
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

internal class ApiRepositoryTest {

    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var apiService: ApiService

    @BeforeEach
    private fun init() {
        MockitoAnnotations.initMocks(this)
        apiRepository = ApiRepository(apiService)
    }

    @Nested
    inner class GetApiMoviesReturnMovies {

        @Test
        fun categoryPopular() {
            val expectedMovies = TestUtil.movies
            val apiResponse = TestUtil.apiResponse
            val returnedApiResponse = Single.just(apiResponse)
            `when`(apiService.getPopularMovies(anyString(), anyInt())).thenReturn(returnedApiResponse)

            val actual = apiRepository.getApiMovies(Category.POPULAR, 4).blockingFirst()

            verify(apiService).getPopularMovies(anyString(), anyInt())
            verifyNoMoreInteractions(apiService)
            assertEquals(Resource.success(expectedMovies), actual)
        }

        @Test
        fun categoryTopRated() {
            val expectedMovies = TestUtil.movies
            val apiResponse = TestUtil.apiResponse
            val returnedApiResponse = Single.just(apiResponse)
            `when`(apiService.getTopRatedMovies(anyString(), anyInt())).thenReturn(returnedApiResponse)

            val actual = apiRepository.getApiMovies(Category.TOP_RATED, 6).blockingFirst()

            verify(apiService).getTopRatedMovies(anyString(), anyInt())
            verifyNoMoreInteractions(apiService)
            assertEquals(Resource.success(expectedMovies), actual)
        }

        @Test
        fun categoryUpcoming() {
            val expectedMovies = TestUtil.movies
            val apiResponse = TestUtil.apiResponse
            val returnedApiResponse = Single.just(apiResponse)
            `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedApiResponse)

            val actual = apiRepository.getApiMovies(Category.UPCOMING, 1).blockingFirst()

            verify(apiService).getUpcomingMovies(anyString(), anyInt())
            verifyNoMoreInteractions(apiService)
            assertEquals(Resource.success(expectedMovies), actual)
        }

    }

    @Nested
    inner class GetApiMoviesReturnError {

        @Test
        fun noMoviesReturned() {
            val expectedMessage = "Couldn't download more movies. Try again."
            val returnedEmptyData = Single.just(ApiResponse())
            `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedEmptyData)

            val actual = apiRepository.getApiMovies(Category.UPCOMING, 1).blockingFirst()

            assertEquals(Resource.error(expectedMessage, null), actual)
        }

        @Test
        fun noMorePagesException() {
            val expectedMessage = "No more pages in this category."
            val networkCallStatus = "HTTP 422"
            `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
                .thenReturn(Single.error(Exception(networkCallStatus)))

            val actual = apiRepository.getApiMovies(Category.UPCOMING, 5).blockingFirst()

            assertEquals(Resource.error(expectedMessage, null), actual)
        }

        @Test
        fun invalidApiKeyException() {
            val expectedMessage = "Invalid API key. Contact technical support."
            val networkCallStatus = "HTTP 401"
            `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
                .thenReturn(Single.error(Exception(networkCallStatus)))

            val actual = apiRepository.getApiMovies(Category.UPCOMING, 5).blockingFirst()

            assertEquals(Resource.error(expectedMessage, null), actual)
        }

        @Test
        fun otherException() {
            val expectedMessage = "Problem with downloading movies. Close app and try again."
            `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
                .thenReturn(Single.error(Exception()))

            val actual = apiRepository.getApiMovies(Category.UPCOMING, 5).blockingFirst()

            assertEquals(Resource.error(expectedMessage, null), actual)
        }

    }

}