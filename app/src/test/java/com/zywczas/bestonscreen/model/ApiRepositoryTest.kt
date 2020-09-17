package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.webservice.ApiResponse
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Single
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


internal class ApiRepositoryTest {

    //todo dac 1 test uruchamiajacy wszystkie repo

    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var expectedMovies : List<Movie>
    private lateinit var apiResponse : ApiResponse
    private lateinit var returnedData : Single<ApiResponse>

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        apiRepository = ApiRepository(apiService)
        expectedMovies = TestUtil.movies
        apiResponse = TestUtil.apiResponse
        returnedData = Single.just(apiResponse)
    }

    @Test
    fun getApiMovies_returnListOfMovies_categoryPopular(){
        val category = Category.POPULAR
        `when`(apiService.getPopularMovies(anyString(), anyInt())).thenReturn(returnedData)

        val returnedValue = apiRepository.getApiMovies(category, 4).blockingFirst()

        verify(apiService).getPopularMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), returnedValue)
    }

    @Test
    fun getApiMovies_returnListOfMovies_categoryTopRated(){
        val category = Category.TOP_RATED
        `when`(apiService.getTopRatedMovies(anyString(), anyInt())).thenReturn(returnedData)

        val returnedValue = apiRepository.getApiMovies(category, 6).blockingFirst()

        verify(apiService).getTopRatedMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), returnedValue)
    }

    @Test
    fun getApiMovies_returnListOfMovies_categoryUpcoming(){
        val category = Category.UPCOMING
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedData)

        val returnedValue = apiRepository.getApiMovies(category, 1).blockingFirst()

        verify(apiService).getUpcomingMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), returnedValue)
    }

    @Test
    fun getApiMovies_noMovies_returnError(){
        val category = Category.UPCOMING
        val emptyApiResponse= ApiResponse()
        val returnedEmptyData = Single.just(emptyApiResponse)
        val expectedMessage = apiRepository.noMoviesError
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedEmptyData)

        val returnedValue = apiRepository.getApiMovies(category, 1).blockingFirst()

        assertEquals(Resource.error(expectedMessage, null), returnedValue)
    }

    @Test
    fun getApiMovies_noMorePagesException_returnError(){
        val category = Category.UPCOMING
        val apiServiceCallStatus = apiRepository.noMorePagesStatus
        val exception = Exception(apiServiceCallStatus)
        val expectedMessage = apiRepository.noMorePagesError
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(Single.error(exception))

        val returnedValue = apiRepository.getApiMovies(category, 5).blockingFirst()

        assertEquals(Resource.error(expectedMessage, null), returnedValue)
    }

    @Test
    fun getApiMovies_invalidApiKeyException_returnError(){
        val category = Category.UPCOMING
        val apiServiceCallStatus = apiRepository.invalidApiKeyStatus
        val exception = Exception(apiServiceCallStatus)
        val expectedMessage = apiRepository.invalidApiKeyError
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(Single.error(exception))

        val returnedValue = apiRepository.getApiMovies(category, 5).blockingFirst()

        assertEquals(Resource.error(expectedMessage, null), returnedValue)
    }

    @Test
    fun getApiMovies_otherException_returnError(){
        val category = Category.UPCOMING
        val exception = Exception()
        val expectedMessage = apiRepository.generalApiError
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(Single.error(exception))

        val returnedValue = apiRepository.getApiMovies(category, 5).blockingFirst()

        assertEquals(Resource.error(expectedMessage, null), returnedValue)
    }

}