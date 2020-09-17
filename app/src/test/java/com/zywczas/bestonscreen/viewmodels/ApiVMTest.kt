package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
internal class ApiVMTest{

    private lateinit var viewModel : ApiVM

    @Mock
    private lateinit var repo : ApiRepository

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        viewModel = ApiVM(repo)
    }

    private fun <Category> anyCategory(): Category = any()

    @Test
    fun getNextMovies_observeChange(){
        val category = Category.POPULAR
        val movies = TestUtil.movies
        val returnedData = Flowable.just(Resource.success(movies))
        `when`(repo.getApiMovies(anyCategory(), anyInt())).thenReturn(returnedData)

        viewModel.getNextMovies(category)
        val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

        assertEquals(Resource.success(Pair(movies, category)), actual)
    }

    @Test
    fun getNextMovies_getError_observeError(){
        val category = Category.UPCOMING
        val message = "some error"
        val returnedData : Flowable<Resource<List<Movie>>> = Flowable.just(Resource.error(message, null))
        `when`(repo.getApiMovies(anyCategory(), anyInt())).thenReturn(returnedData)

        viewModel.getNextMovies(category)
        val actual = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)

        verify(repo).getApiMovies(anyCategory(), anyInt())
        verifyNoMoreInteractions(repo)
        assertEquals(Resource.error(message, Pair(emptyList<Movie>(), category)), actual)
    }


}