package com.zywczas.bestonscreen.views

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zywczas.bestonscreen.R
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
internal class DBActivityTest {

    @Test
    fun isActivityInView(){
        val scenario = ActivityScenario.launch(DBActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.drawer_layout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun visibility_toolbar_RecyclerView_progressbar_textView(){
        val scenario = ActivityScenario.launch(DBActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.moviesRecyclerView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }

}