package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.util.TestUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import io.reactivex.Flowable
import org.junit.jupiter.api.Assertions

internal class DBRepositoryTest {

    //system under test
    private lateinit var dbRepo : DBRepository

    @Mock
    private lateinit var movieDao: MovieDao

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        dbRepo = DBRepository(movieDao)
    }

    @Throws(Exception::class)
    @Test
    fun getMoviesFromDb_returnMovieList(){
        //arrange
        val movies = TestUtil.movies
        val moviesFromDB = TestUtil.moviesFromDb
        val returnedData = Flowable.just(moviesFromDB)
        Mockito.`when`(movieDao.getMovies()).thenReturn(returnedData)
        //act
        val returnedValue = dbRepo.getMoviesFromDB().blockingFirst()
        //assert
        Mockito.verify(movieDao).getMovies()
        Mockito.verifyNoMoreInteractions(movieDao)
        Assertions.assertEquals(movies, returnedValue)

    }

    @Throws(Exception::class)
    @Test
    fun getMoviesFromDB_emptyDB_returnEmptyList(){
        //arrange
        val moviesFromDB = listOf<MovieFromDB>()
        val returnedData = Flowable.just(moviesFromDB)
        Mockito.`when`(movieDao.getMovies()).thenReturn(returnedData)
        //act
        val returnedValue = dbRepo.getMoviesFromDB().blockingFirst()
        //assert
        Assertions.assertEquals(listOf<Movie>(), returnedValue)
    }


}