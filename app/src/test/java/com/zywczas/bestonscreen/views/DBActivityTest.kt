package com.zywczas.bestonscreen.views

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith

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