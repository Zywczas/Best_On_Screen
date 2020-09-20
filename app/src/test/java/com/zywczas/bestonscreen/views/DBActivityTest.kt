package com.zywczas.bestonscreen.views

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.util.TestUtil
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.RuntimeEnvironment.application
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
internal class DBActivityTest {

    private lateinit var app : App
    private lateinit var picasso: Picasso

    @Before
    fun init(){
        app = ApplicationProvider.getApplicationContext()

    }

    @Test
    fun testIncrementByOne() {
        assertEquals(1, 1)
    }


    @Test
    fun isActivityInView(){
        val scenario = ActivityScenario.launch(DBActivity::class.java)
        scenario.moveToState(Lifecycle.State.CREATED)
        onView(withId(R.id.drawer_layout))
            .check(matches(isDisplayed()))
    }

//    @Test
//    fun visibility_toolbar_RecyclerView_progressbar_textView(){
//        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
//        onView(withId(R.id.moviesRecyclerView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
//        onView(withId(R.id.progressBar)).check(matches(CoreMatchers.not(isDisplayed())))
//        onView(withId(R.id.emptyListTextView)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun isContentDisplayed(){
//        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Movies: My List"))))
//        //todo dodac filmy w recycler view
//    }
//
//    @Test
//    fun selectListItem_isDetailActivityAndCorrectMovieVisible(){
//        val listItem = 0
//        val movie = TestUtil.movies[listItem]
//
//        onView(withId(R.id.moviesRecyclerView))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<MovieAdapter.ViewHolder>(
//                    listItem,
//                    ViewActions.click()
//                )
//            )
//
//        onView(withId(R.id.movieDetailsLayout))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.titleTextViewDetails))
//            .check(matches(withText(movie.title)))
//    }
//    @Test
//    fun selectListItem_goToDetailActivity_goBack(){
//        onView(withId(R.id.moviesRecyclerView)).perform()
//    }


    /*
    czy lista filmow jet widoczn
     */


    /*
    drawer i kazda z opcji w nich i czy widoczne
     */

    /*
    obracanie eranu
     */

}