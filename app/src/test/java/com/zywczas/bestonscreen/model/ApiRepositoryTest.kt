package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

internal class ApiRepositoryTest {

    //todo pomyslec nad osobnym testem dla interfejsu

    //system under test
    private lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var apiService: ApiService

    private val expectedMovies = TestUtil.movies
    private val expectedTotalPages = TestUtil.totalPagesInCategory
    private val apiResponse = TestUtil.apiResponse
    private val returnedData = Single.just(apiResponse)

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        apiRepository = ApiRepository(apiService)
    }

    @Throws(Exception::class)
    @Test
    fun getApiMovies_returnListOfMovies_categoryPopular(){
        //arrange
        val category = Category.POPULAR
        Mockito.`when`(apiService.getPopularMovies(anyString(), anyInt())).thenReturn(returnedData)
        //act
        val returnedValue = apiRepository.getApiMovies(category, 4).blockingFirst()
        //assert
        Mockito.verify(apiService).getPopularMovies(anyString(), anyInt())
        Mockito.verifyNoMoreInteractions(apiService)
        Assertions.assertEquals(Resource.success(Pair(expectedMovies, expectedTotalPages)), returnedValue)
    }

    @Throws(Exception::class)
    @Test
    fun getApiMovies_returnListOfMovies_categoryTopRated(){
        //arrange
        val category = Category.TOP_RATED
        Mockito.`when`(apiService.getTopRatedMovies(anyString(), anyInt())).thenReturn(returnedData)
        //act
        val returnedValue = apiRepository.getApiMovies(category, 6).blockingFirst()
        //assert
        Mockito.verify(apiService).getTopRatedMovies(anyString(), anyInt())
        Mockito.verifyNoMoreInteractions(apiService)
        Assertions.assertEquals(Resource.success(Pair(expectedMovies, expectedTotalPages)), returnedValue)
    }

    @Throws(Exception::class)
    @Test
    fun getApiMovies_returnListOfMovies_categoryUpcoming(){
        //arrange
        val category = Category.UPCOMING
        Mockito.`when`(apiService.getUpcomingMovies(anyString(), anyInt())).thenReturn(returnedData)
        //act
        val returnedValue = apiRepository.getApiMovies(category, 1).blockingFirst()
        //assert
        Mockito.verify(apiService).getUpcomingMovies(anyString(), anyInt())
        Mockito.verifyNoMoreInteractions(apiService)
        Assertions.assertEquals(Resource.success(Pair(expectedMovies, expectedTotalPages)), returnedValue)
    }



    /*
    czy wysyla blad jak nie ma wiecej stron
     */

    /*
    czy wysyla blad jak nie moze pobrac filmow
     */

    /*
        blad jak zla strona, np -1
     */

    /*
    blad jak zly api key
     */


}