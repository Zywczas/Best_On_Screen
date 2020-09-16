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
    //todo do testowania wszystkie lasy i funkcje testowane musza byc open

    //system under test
    private lateinit var viewModel : ApiVM

    @Mock
    private lateinit var repo : ApiRepository

    @BeforeEach
    private fun init(){
        MockitoAnnotations.openMocks(this)
        viewModel = ApiVM(repo)
    }

    private fun <Category> anyCategory(): Category = any<Category>()

    @Test
    fun observeEmptyMoviesAndCategoryWhenLiveDataSet(){
        //act
        val returnedValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
        //assert
        assertNull(returnedValue)
    }

    @Test
    fun getNextMovies_observeChange(){
        //arrange
        val category = Category.POPULAR
        val movies = TestUtil.movies
        val returnedData = Flowable.just(Resource.success(movies))
        `when`(repo.getApiMovies(anyCategory(), anyInt())).thenReturn(returnedData)
        //act
        viewModel.getNextMovies(category)
        val returnedValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
        //assert
        assertEquals(Resource.success(Pair(movies, category)), returnedValue)
    }

    @Test
    fun getNextMovies_getError_observeError(){
        //arrange
        val category = Category.UPCOMING
        val message = "some error"
        val expectedValue = Resource.error(message, Pair(emptyList<Movie>(), category))
        val returnedData : Flowable<Resource<List<Movie>>> = Flowable.just(Resource.error(message, null))
        `when`(repo.getApiMovies(anyCategory(), anyInt())).thenReturn(returnedData)
        //act
        viewModel.getNextMovies(category)
        val returnedValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
        //assert
        verify(repo).getApiMovies(anyCategory(), anyInt())
        verifyNoMoreInteractions(repo)
        assertEquals(expectedValue, returnedValue)
    }


}