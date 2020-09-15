package com.zywczas.bestonscreen.viewmodels

import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import com.zywczas.bestonscreen.utilities.Resource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.internal.operators.single.SingleToFlowable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExtendWith(InstantExecutorExtension::class)
internal class ApiVMTest{

    //system under test
    private lateinit var viewModel : ApiVM

    @Mock
    lateinit var repo : ApiRepository
    private lateinit var movies: List<Movie>


    @BeforeEach
    private fun init(){
        MockitoAnnotations.openMocks(this)
        viewModel = ApiVM(repo)
        movies = TestUtil.movies
    }

    private fun <Category> anyCategory(): Category = any<Category>()

    @Test
    fun observeEmptyMoviesAndCategoryWhenLiveDataSet(){
        //act
        val returnedValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
        val returnedMovies : List<Movie>? = returnedValue?.data?.first
        val returnedCategory : Category? = returnedValue?.data?.second
        //assert
        assertNull(returnedMovies)
        assertNull(returnedCategory)
    }

    @Test
    fun getNextMovies_popularCategory_observeChange(){
        //arrange
        val category = Category.POPULAR
        val returnedData = Flowable.just(Resource.success(movies))
        `when`(repo.getApiMovies(anyCategory(), anyInt())).thenReturn(returnedData)
        //act
        viewModel.getNextMovies(category)
        val returnedValue = LiveDataTestUtil.getValue(viewModel.moviesAndCategoryLD)
        //assert
        assertEquals(Resource.success(Pair(movies, category)), returnedValue)
    }

//todo do testowania wszystkie lasy i funkcje testowane musza byc open
}