package com.zywczas.bestonscreen.model.db

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zywczas.bestonscreen.util.TestUtil
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

internal class MovieDaoTest : MoviesDataBaseTest() {

    private val movieFromDb1 = TestUtil.movieFromDB1
    private val movieFromDb2 = TestUtil.movieFromDB2

    //it has to have @JvmField annotation to 'make this rule public' - otherwise gives an error in Kotlin
    @Rule @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun insert(){
        dao.insertMovie(movieFromDb1).blockingGet()
        val movieIdCount = dao.getIdCount(movieFromDb1.id).blockingFirst()

        assertEquals(1, movieIdCount)
    }

    @Test
    fun insert_delete(){
        dao.insertMovie(movieFromDb1).blockingGet()
        val moviesDeleted = dao.deleteMovie(movieFromDb1).blockingGet()
        val movieIdCount = dao.getIdCount(movieFromDb1.id).blockingFirst()

        assertEquals(1, moviesDeleted)
        assertEquals(0, movieIdCount)
    }

    @Test
    fun insert2Movies_getList(){
        val expectedValue = TestUtil.moviesFromDb

        dao.insertMovie(movieFromDb1).blockingGet()
        dao.insertMovie(movieFromDb2).blockingGet()
        val returnedValue = dao.getMovies().blockingFirst()

        assertEquals(expectedValue, returnedValue)
    }

    @Test
    fun insert2Movies_deleteThem_getEmptyList(){
        val expectedValue = emptyList<MovieFromDB>()

        dao.insertMovie(movieFromDb1).blockingGet()
        dao.insertMovie(movieFromDb2).blockingGet()
        dao.deleteMovie(movieFromDb1).blockingGet()
        dao.deleteMovie(movieFromDb2).blockingGet()
        val returnedValue = dao.getMovies().blockingFirst()

        assertEquals(expectedValue, returnedValue)
    }

    @Test
    fun insert_insertAgain_getException(){
        dao.insertMovie(movieFromDb1).blockingGet()

        assertThrows(SQLiteConstraintException::class.java) {
            dao.insertMovie(movieFromDb1).blockingGet()
        }
    }

    @Test
    fun delete_movieNotInDb(){
        val moviesRemoved = dao.deleteMovie(movieFromDb1).blockingGet()

        assertEquals(0, moviesRemoved)
    }

}