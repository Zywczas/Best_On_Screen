package com.zywczas.bestonscreen.model

import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.webservice.ApiService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class ApiRepositoryTest {

    //todo pomyslec nad osobnym testem dla interfejsu

    //system under test
    lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var compositeDisposable: CompositeDisposable
    @Mock
    lateinit var movies: ArrayList<Movie>
    @Mock
    lateinit var apiService: ApiService
    @Mock
    lateinit var moviesLiveData : MutableLiveData<Triple<List<Movie>, Int, Category>>

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        apiRepository = ApiRepository(compositeDisposable, movies, apiService, moviesLiveData)
    }

    /*
    czy live data dziala z observer
     */

    /*
    czy nie dziala bez observera
     */

    /*
    czy pobiera filmy
     */

    /*
    czy wysyla blad jak nie ma wiecej stron
     */

    /*
    czy wysyla blad jak nie moze pobrac filmow
     */

    /*

     */
}