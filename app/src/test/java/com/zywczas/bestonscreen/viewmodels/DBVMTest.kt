package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.DBRepository
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.internal.operators.single.SingleToFlowable
import io.reactivex.rxjava3.kotlin.Flowables
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
internal class DBVMTest {

    //system under test
    private lateinit var viewModel : DBVM

    @Mock
    lateinit var repo : DBRepository

    @BeforeEach
    private fun init(){
        MockitoAnnotations.openMocks(this)
        viewModel = DBVM(repo)
    }

    @Test //todo livedatatestutil chyba nie wzbudza livedaty, moze zamienic na ta druga funkcje i sprawdzic
    fun observeMoviesWhenLiveDataSet(){
        //arrange
        val movies = TestUtil.movies
        val returnedData = Flowable.just(movies)
        `when`(repo.getMoviesFromDB()).thenReturn(returnedData)
        //act
        val returnedValue = LiveDataTestUtil.getValue(viewModel.moviesLD)
        //assert

//        assertEquals(movies, data)
    }


}
