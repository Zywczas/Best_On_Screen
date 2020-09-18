package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.webservice.ApiResponse
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


internal class ApiRepositoryTest {

    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var apiService: ApiService

    private val expectedMovies = TestUtil.movies
    private val apiResponse = TestUtil.apiResponse
    private val returnedData = Single.just(apiResponse)

    @BeforeEach
    private fun init() {
        MockitoAnnotations.initMocks(this)
        apiRepository = ApiRepository(apiService)
    }

    @Test
    fun getApiMovies_categoryPopular_returnListOfMovies() {
        `when`(apiService.getPopularMovies(anyString(), anyInt())).thenReturn(returnedData)

        val actual = apiRepository.getApiMovies(Category.POPULAR, 4).blockingFirst()

        verify(apiService).getPopularMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), actual)
    }

    @Test
    fun getApiMovies_categoryTopRated_returnListOfMovies() {
        `when`(apiService.getTopRatedMovies(anyString(), anyInt())).thenReturn(returnedData)

        val actual = apiRepository.getApiMovies(Category.TOP_RATED, 6).blockingFirst()

        verify(apiService).getTopRatedMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), actual)
    }

    @Test
    fun getApiMovies_categoryUpcoming_returnListOfMovies() {
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedData)

        val actual = apiRepository.getApiMovies(Category.UPCOMING, 1).blockingFirst()

        verify(apiService).getUpcomingMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), actual)
    }

    @Test
    fun getApiMovies_noMoviesReturned_returnError() {
        val expectedMessage = apiRepository.noMoviesError
        val returnedEmptyData = Single.just(ApiResponse())
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedEmptyData)

        val actual = apiRepository.getApiMovies(Category.UPCOMING, 1).blockingFirst()

        assertEquals(Resource.error(expectedMessage, null), actual)
    }

    @Test
    fun getApiMovies_noMorePagesException_returnError() {
        val expectedMessage = apiRepository.noMorePagesError
        val networkCallStatus = apiRepository.noMorePagesStatus
        `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
            .thenReturn(Single.error(Exception(networkCallStatus)))

        val actual = apiRepository.getApiMovies(Category.UPCOMING, 5).blockingFirst()

        assertEquals(Resource.error(expectedMessage, null), actual)
    }

    @Test
    fun getApiMovies_invalidApiKeyException_returnError() {
        val expectedMessage = apiRepository.invalidApiKeyError
        val networkCallStatus = apiRepository.invalidApiKeyStatus
        `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
            .thenReturn(Single.error(Exception(networkCallStatus)))

        val actual = apiRepository.getApiMovies(Category.UPCOMING, 5).blockingFirst()

        assertEquals(Resource.error(expectedMessage, null), actual)
    }

    @Test
    fun getApiMovies_otherException_returnError() {
        val expectedMessage = apiRepository.generalApiError
        `when`(apiService.getUpcomingMovies(anyString(), anyInt()))
            .thenReturn(Single.error(Exception()))

        val actual = apiRepository.getApiMovies(Category.UPCOMING, 5).blockingFirst()

        assertEquals(Resource.error(expectedMessage, null), actual)
    }

}