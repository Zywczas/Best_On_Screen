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
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*


internal class DBRepositoryTest {

    private lateinit var dbRepo : DBRepository

    @Mock
    private lateinit var movieDao: MovieDao

    @BeforeEach
    private fun init(){
        MockitoAnnotations.initMocks(this)
        dbRepo = DBRepository(movieDao)
    }

    @Test
    fun getMoviesFromDb_returnMovieList(){
        val movies = TestUtil.movies
        val moviesFromDB = TestUtil.moviesFromDb
        val returnedData = Flowable.just(moviesFromDB)
        `when`(movieDao.getMovies()).thenReturn(returnedData)

        val returnedValue = dbRepo.getMoviesFromDB().blockingFirst()

        verify(movieDao).getMovies()
        verifyNoMoreInteractions(movieDao)
        assertEquals(movies, returnedValue)

    }

    @Test
    fun getMoviesFromDB_emptyDB_returnEmptyList(){
        val moviesFromDB = listOf<MovieFromDB>()
        val returnedData = Flowable.just(moviesFromDB)
        `when`(movieDao.getMovies()).thenReturn(returnedData)

        val returnedValue = dbRepo.getMoviesFromDB().blockingFirst()

        assertEquals(listOf<Movie>(), returnedValue)
    }


}