package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.webservice.ApiResponse
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


internal class ApiRepositoryTest {

    //todo pomyslec nad osobnym testem dla interfejsu

    //todo dac 1 test uruchamiajacy wszystkie repo

    //system under test
    private lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var apiService: ApiService
    private lateinit var expectedMovies : List<Movie>
    private lateinit var apiResponse : ApiResponse
    private lateinit var returnedData : Single<ApiResponse>

    @BeforeEach
    private fun init(){
        MockitoAnnotations.openMocks(this)
        apiRepository = ApiRepository(apiService)
        expectedMovies = TestUtil.movies
        apiResponse = TestUtil.apiResponse
        returnedData = Single.just(apiResponse)
    }

    @Test
    fun getApiMovies_returnListOfMovies_categoryPopular(){
        //arrange
        val category = Category.POPULAR
        `when`(apiService.getPopularMovies(anyString(), anyInt())).thenReturn(returnedData)
        //act
        val returnedValue = apiRepository.getApiMovies(category, 4).blockingFirst()
        //assert
        verify(apiService).getPopularMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), returnedValue)
    }

    @Test
    fun getApiMovies_returnListOfMovies_categoryTopRated(){
        //arrange
        val category = Category.TOP_RATED
        `when`(apiService.getTopRatedMovies(anyString(), anyInt())).thenReturn(returnedData)
        //act
        val returnedValue = apiRepository.getApiMovies(category, 6).blockingFirst()
        //assert
        verify(apiService).getTopRatedMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), returnedValue)
    }

    @Test
    fun getApiMovies_returnListOfMovies_categoryUpcoming(){
        //arrange
        val category = Category.UPCOMING
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedData)
        //act
        val returnedValue = apiRepository.getApiMovies(category, 1).blockingFirst()
        //assert
        verify(apiService).getUpcomingMovies(anyString(), anyInt())
        verifyNoMoreInteractions(apiService)
        assertEquals(Resource.success(expectedMovies), returnedValue)
    }

    //todo dodac test dla pustej listy filmow

    @Test
    fun getApiMovies_noMorePagesException_returnError(){
        //arrange
        val category = Category.UPCOMING
        val apiServiceCallStatus = apiRepository.noMorePagesStatus
        val exception = Exception(apiServiceCallStatus)
        val expectedMessage = apiRepository.noMorePagesMessage
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(Single.error(exception))
        //act
        val returnedValue = apiRepository.getApiMovies(category, 5).blockingFirst()
        //assert
        assertEquals(Resource.error(expectedMessage, null), returnedValue)
    }

    @Test
    fun getApiMovies_invalidApiKeyException_returnError(){
        //arrange
        val category = Category.UPCOMING
        val apiServiceCallStatus = apiRepository.invalidApiKeyStatus
        val exception = Exception(apiServiceCallStatus)
        val expectedMessage = apiRepository.invalidApiKeyMessage
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(Single.error(exception))
        //act
        val returnedValue = apiRepository.getApiMovies(category, 5).blockingFirst()
        //assert
        assertEquals(Resource.error(expectedMessage, null), returnedValue)
    }

    @Test
    fun getApiMovies_otherException_returnError(){
        //arrange
        val category = Category.UPCOMING
        val exception = Exception()
        val expectedMessage = apiRepository.generalApiError
        `when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(Single.error(exception))
        //act
        val returnedValue = apiRepository.getApiMovies(category, 5).blockingFirst()
        //assert
        assertEquals(Resource.error(expectedMessage, null), returnedValue)
    }

}