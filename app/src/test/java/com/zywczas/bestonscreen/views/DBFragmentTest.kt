package com.zywczas.bestonscreen.views

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class DBFragmentTest {

    @Test
    fun test1(){

    }
}

//    @Rule
//    @JvmField
//    val activityRule = ActivityScenarioRule(DBActivity::class.java)
//
//    @Test
//    fun isActivityInView() {
//        onView(withId(R.id.drawer_layout))
//            .check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun visibility_toolbar_RecyclerView_progressbar_textView() {
//        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
//        onView(withId(R.id.moviesRecyclerView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
//        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
//        onView(withId(R.id.emptyListTextView)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun isContentDisplayed() {
//        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Movies: My List"))))
//
//    }