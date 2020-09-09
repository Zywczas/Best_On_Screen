package com.zywczas.bestonscreen.model

import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.webservice.ApiService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class ApiRepositoryTest {

    //todo pomyslec nad osobnym testem dla interfejsu

    //system under test
    lateinit var apiRepository: ApiRepository

    @Mock
    lateinit var movies: ArrayList<Movie>
    @Mock
    lateinit var apiService: ApiService

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        apiRepository = ApiRepository(movies, apiService)
    }

    /*
    czy pobiera filmy i zwraca pojedynczy wynik tylko raz i nic wiecej
     */

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