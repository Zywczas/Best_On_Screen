package com.zywczas.bestonscreen.model.webservice

import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@ExtendWith(InstantExecutorExtension::class)
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
        //arrange
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(TestUtil.restApiResponseBody)
        mockWebServer.enqueue(response)
        val expectedMovie3Id = 724989
        //act
        val moviesFromApi = apiService.getPopularMovies(anyString(), anyInt()).blockingGet().movies
        val returnedMovie3Id = moviesFromApi?.get(2)?.id
        //assert
        assertEquals(20, moviesFromApi?.size)
        assertEquals(expectedMovie3Id, returnedMovie3Id)
    }

//todo stworzyc nowy film z danymi z listy i porownac z tym otrzymanym przez api service

}

