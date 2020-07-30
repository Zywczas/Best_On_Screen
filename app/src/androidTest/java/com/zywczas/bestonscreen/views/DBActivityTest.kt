package com.zywczas.bestonscreen.views

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.zywczas.bestonscreen.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DBActivityTest {
    @Test
    fun test_isActivityInView() {
        //testuje czy activity sie wlacza
        val activityScenario = ActivityScenario.launch(DBActivity::class.java)

        //czy layout jest pokazywany
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
    }
}