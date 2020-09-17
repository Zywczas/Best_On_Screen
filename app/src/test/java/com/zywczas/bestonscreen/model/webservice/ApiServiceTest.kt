package com.zywczas.bestonscreen.model.webservice

import com.zywczas.bestonscreen.util.MockedApiResponseBody
import com.zywczas.bestonscreen.util.TestUtil
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ApiServiceTest {

    private val mockWebServer = MockWebServer()
    private lateinit var apiService: ApiService

    @BeforeAll
    private fun init(){
        mockWebServer.start()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @AfterAll
    private fun close(){
        mockWebServer.shutdown()
    }

    @Test
    fun getMovies(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockedApiResponseBody.body)
        mockWebServer.enqueue(response)

        val movies = apiService.getPopularMovies("anyString", 777).blockingGet().movies
        val actualMoviesCount = movies?.size

        assertEquals(20, actualMoviesCount)
    }

    @Test
    fun getMovies_gsonConverter(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockedApiResponseBody.body)
        mockWebServer.enqueue(response)
        val expected3rdMovie = TestUtil.movieFromApi1

        val movies = apiService.getPopularMovies("anyString", 777).blockingGet().movies
        val actual3rdMovie = movies?.get(2)

        assertEquals(expected3rdMovie, actual3rdMovie)
    }

    @Test
    fun getMovies_throwException(){
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
            .setBody(MockedApiResponseBody.body)
        mockWebServer.enqueue(response)

        assertThrows(HttpException::class.java) {
            apiService.getPopularMovies("anyString", 777).blockingGet()
        }
    }


}

