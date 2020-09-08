package com.zywczas.bestonscreen.views

import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DetailsActivityTest {

    @Test
    fun test_isActivityInView() {
        //testuje czy activity sie wlacza
        val activityScenario = ActivityScenario.launch(DetailsActivity::class.java)

        //czy layout jest pokazywany
//        onView(withId(R.id.movieDetailsLayout))
//            .check(matches(isDisplayed()))
    }

}