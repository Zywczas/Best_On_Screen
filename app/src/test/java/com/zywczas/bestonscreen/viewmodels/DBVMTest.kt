package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.DBRepository
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
internal class DBVMTest {

    //system under test
    private lateinit var viewModel: DBVM

    @Mock
    private lateinit var repo: DBRepository
    private val movies = TestUtil.movies

    @BeforeEach
    private fun init() {
        MockitoAnnotations.openMocks(this)
        val returnedData = Flowable.just(movies)
        `when`(repo.getMoviesFromDB()).thenReturn(returnedData)
        viewModel = DBVM(repo)
    }

    //todo przeniesc na zewnatrz
//    fun <T> LiveData<T>.test() : TestObserver<T> {
//        return TestObserver.create()
//    }

    @Test
    fun observeMoviesWhenLiveDataSet() {
        //act
        val returnedValue = LiveDataTestUtil.getValue(viewModel.moviesLD)
        //assert
        assertEquals(movies, returnedValue)
    }


}
