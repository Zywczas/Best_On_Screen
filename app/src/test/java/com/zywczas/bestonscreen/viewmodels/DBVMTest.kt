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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExtendWith(InstantExecutorExtension::class)
internal class DBVMTest {

    private lateinit var viewModel: DBVM
    private val repo = mock(DBRepository::class.java)
    private val expectedMovies = TestUtil.moviesList1_2

    @BeforeEach
    private fun init() {
        `when`(repo.getMoviesFromDB()).thenReturn(Flowable.just(expectedMovies))
        viewModel = DBVM(repo)
    }

    @Test
    fun observeMoviesWhenLiveDataSet() {
        val actualMovies = LiveDataTestUtil.getValue(viewModel.moviesLD)

        assertEquals(expectedMovies, actualMovies)
    }


}
