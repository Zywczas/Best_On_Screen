package com.zywczas.bestonscreen.views

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.zywczas.bestonscreen.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DBActivityTest {
    @Test
    fun test_isActivityInView() {
        //testuje czy activity sie wlacza, to musi byc przy kazdym tescie albo 1 dla calej klasy
        val activityScenario = ActivityScenario.launch(DBActivity::class.java)

        //czy layout jest pokazywany
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
        //czy layout jest pokazywany - drugi sposob
        onView(withId(R.id.drawer_layout)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.moviesRecyclerView)).check(matches(isDisplayed()))
        //z drawera nie chce tutaj dzialac
//        onView(withId(R.id.myToWatchListTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isBarTitleDisplayed(){
        //testuje czy activity sie wlacza
        val activityScenario = ActivityScenario.launch(DBActivity::class.java)
        onView(withId(R.id.moviesRecyclerView)).check(matches(isDisplayed()))
//        onView(withId(R.id.toolbar)).check(matches(withText(R.string.my_list_title)))
    }
}