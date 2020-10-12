package com.zywczas.bestonscreen.model.db

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zywczas.bestonscreen.util.TestUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class MovieDaoTest : MoviesDataBaseTest() {

    private val movieFromDb1 = TestUtil.movieFromDB1
    private val movieFromDb2 = TestUtil.movieFromDB2

    @Test
    fun insert() {
        dao.insertMovie(movieFromDb1).blockingGet()
        val actual = dao.getIdCount(movieFromDb1.id).blockingFirst()

        assertEquals(1, actual)
    }

    @Test
    fun insert_delete() {
        dao.insertMovie(movieFromDb1).blockingGet()
        val actualDeletedMoviesCount = dao.deleteMovie(movieFromDb1).blockingGet()
        val actualMovieIdCount = dao.getIdCount(movieFromDb1.id).blockingFirst()

        assertEquals(1, actualDeletedMoviesCount)
        assertEquals(0, actualMovieIdCount)
    }

    @Test
    fun insertMovies_getList() {
        val expectedMoviesFromDB = TestUtil.moviesFromDb

        dao.insertMovie(movieFromDb1).blockingGet()
        dao.insertMovie(movieFromDb2).blockingGet()
        val actualMoviesFromDB = dao.getMovies().blockingFirst()

        assertEquals(expectedMoviesFromDB, actualMoviesFromDB)
    }

    @Test
    fun insertMovies_deleteThem_getEmptyList() {
        val expectedMoviesFromDB = emptyList<MovieFromDB>()

        dao.insertMovie(movieFromDb1).blockingGet()
        dao.insertMovie(movieFromDb2).blockingGet()
        dao.deleteMovie(movieFromDb1).blockingGet()
        dao.deleteMovie(movieFromDb2).blockingGet()
        val actualMoviesFromDB = dao.getMovies().blockingFirst()

        assertEquals(expectedMoviesFromDB, actualMoviesFromDB)
    }

    @Test
    fun insert_insertAgain_getException() {
        dao.insertMovie(movieFromDb1).blockingGet()

        assertThrows(SQLiteConstraintException::class.java) {
            dao.insertMovie(movieFromDb1).blockingGet()
        }
    }

    @Test
    fun delete_movieNotInDb() {
        val actualMoviesRemoved = dao.deleteMovie(movieFromDb1).blockingGet()

        assertEquals(0, actualMoviesRemoved)
    }

}