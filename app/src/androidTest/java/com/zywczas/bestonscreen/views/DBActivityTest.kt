package com.zywczas.bestonscreen.views

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.zywczas.bestonscreen.R
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test


class DBActivityTest {

    //it has to have @JvmField annotation to 'make this rule public' - otherwise gives an error in Kotlin
    @Rule
    @JvmField
    val activityScenario = ActivityScenarioRule(DBActivity::class.java)

    @Test
    fun isActivityInView(){
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun visibility_toolbar_RecyclerView_progressbar_textView(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.moviesRecyclerView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emptyListTextView)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun toolbarTitle(){
        onView(withId(R.id.toolbar)).check(matches(withText("Movies: My List")))
    }
}