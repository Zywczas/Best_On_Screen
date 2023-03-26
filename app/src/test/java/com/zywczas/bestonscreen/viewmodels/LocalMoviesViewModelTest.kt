package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.repositories.LocalMoviesRepository
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
internal class LocalMoviesViewModelTest {

    private lateinit var viewModel: LocalMoviesViewModel
    private val repo = mock(LocalMoviesRepository::class.java)
    private val expectedMovies = TestUtil.moviesList1_2

    @BeforeEach
    private fun init() {
        `when`(repo.getMoviesFromDB()).thenReturn(Flowable.just(expectedMovies))
        viewModel = LocalMoviesViewModel(repo)
    }

    @Test
    fun observeMoviesWhenLiveDataSet() {
        val actualMovies = LiveDataTestUtil.getValue(viewModel.movies)

        assertEquals(expectedMovies, actualMovies)
    }
}
