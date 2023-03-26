package com.zywczas.bestonscreen.views

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zywczas.bestonscreen.R
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {

    @Rule
    @JvmField
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isActivityInView() {
        onView(withId(R.id.toolbarMovies)).check(matches(isDisplayed()))
        onView(withId(R.id.drawerLayoutMain)).check(matches(isDisplayed())).check(matches(isClosed()))
        onView(withId(R.id.navDrawer)).check(matches(not(isDisplayed())))
    }

    @Test
    fun isFragmentInContainerInView() {
        onView(withId(R.id.recyclerViewDB)).check(matches(isDisplayed()))
    }

    @Test
    fun activityDestroyed_isActivityInViewAfterRecreation() {
        activity.scenario.recreate()
        onView(withId(R.id.toolbarMovies)).check(matches(isDisplayed()))
        onView(withId(R.id.drawerLayoutMain)).check(matches(isDisplayed())).check(matches(isClosed()))
        onView(withId(R.id.navDrawer)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerViewDB)).check(matches(isDisplayed()))
    }

//    @Test
//    fun isDrawerOpening(){
////        onView(withId(R.id.drawerLayoutMain))
////            .check(matches(isDisplayed()))
////            .check(matches(isClosed()))
////            .perform(open())
////            .check(matches(isOpen()))
//        //todo to be fixed - test fails currently
//    }

//    @Test
//    fun isDrawerClosing(){
//        //todo to be implemented after fixing drawer test
//    }

//    @Test
//    fun clickNavMenuButtons_isNavigatingToOtherFragments() {
//        //todo to be implemented after fixing drawer test
//    }
}
