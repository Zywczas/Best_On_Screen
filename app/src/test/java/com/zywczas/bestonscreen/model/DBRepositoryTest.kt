package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
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

    @Test
    fun getMoviesFromDb_returnMovies(){
        //arrange
        val movies = TestUtil.moviesList
        val moviesFromDB = TestUtil.moviesFromDb
        val expectedValue = Resource.success(movies)
        val returnedData = Flowable.just(moviesFromDB)
        Mockito.`when`(movieDao.getMovies()).thenReturn(returnedData)

        //act //todo trzeba dawac blockingFirst przy RxJava
        val returnedValue = dbRepo.getMoviesFromDB().blockingFirst()

        //assert
        Assertions.assertEquals(expectedValue, returnedValue)

    }

}