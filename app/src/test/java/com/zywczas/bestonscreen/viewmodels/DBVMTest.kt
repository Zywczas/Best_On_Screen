package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.LiveData
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
import io.reactivex.rxjava3.observers.TestObserver
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

    //todo przeniesc na zewnatrz
    fun <T> LiveData<T>.test() : TestObserver<T> {
        return TestObserver.create()
    }

    @Test
    fun observeMoviesWhenLiveDataSet(){
        //arrange
        val repo = mock(DBRepository::class.java)
        val movies = TestUtil.movies
        val returnedData = Flowable.just(movies)
        `when`(repo.getMoviesFromDB()).thenReturn(returnedData)
        val viewModel = DBVM(repo)
        //act
        val returnedValue = LiveDataTestUtil.getValue(viewModel.moviesLD)
        //assert
        assertEquals(movies, returnedValue)
    }


}
