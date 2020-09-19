package com.zywczas.bestonscreen.views

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.util.TestUtil
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test


class DBActivityTest {

    //it has to have @JvmField annotation to 'make this rule public' - otherwise gives an error in Kotlin
    @Rule
    @JvmField
    val activityScenarioRule = ActivityScenarioRule(DBActivity::class.java)

//    @Rule
//    @JvmField
//    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Test
    fun isActivityInView(){
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun visibility_toolbar_RecyclerView_progressbar_textView(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.moviesRecyclerView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emptyListTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun isContentDisplayed(){
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Movies: My List"))))
        //todo dodac filmy w recycler view
    }

    @Test
    fun selectListItem_isDetailActivityAndCorrectMovieVisible(){
        val listItem = 0
        val movie = TestUtil.movies[listItem]

        onView(withId(R.id.moviesRecyclerView))
            .perform(actionOnItemAtPosition<MovieAdapter.ViewHolder>(listItem, click()))

        onView(withId(R.id.movieDetailsLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.titleTextViewDetails)).check(matches(withText(movie.title)))
    }
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