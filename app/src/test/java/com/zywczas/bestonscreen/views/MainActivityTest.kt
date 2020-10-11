package com.zywczas.bestonscreen.views

import android.app.Activity
import android.view.Gravity
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zywczas.bestonscreen.R
import org.hamcrest.core.IsNot.*
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

    //todo usunac
    private fun <T : Activity> ActivityScenarioRule<T>.getToolbarNavContentDescription() : String {
        var description = ""
        scenario.onActivity {
            description = it.findViewById<Toolbar>(R.id.toolbarMovies).navigationContentDescription.toString()
        }
        return description
    }

    @Test
    fun isActivityInView() {
        onView(withId(R.id.toolbarMovies)).check(matches(isDisplayed()))
        onView(withId(R.id.drawerLayoutMain)).check(matches(isDisplayed())).check(matches(isClosed()))
        onView(withId(R.id.navDrawer)).check(matches(not(isDisplayed())))
    }

    @Test
    fun isFragmentInContainerInView(){
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

    @Test
    fun isDrawerOpening(){
        onView(withId(R.id.drawerLayoutMain))
            .check(matches(isDisplayed()))
            .check(matches(DrawerMatchers.isClosed()))
            .perform(swipeRight())
            .check(matches(DrawerMatchers.isOpen()))
    }

    @Test
    fun navigationToDetailsFragment() {
        onView(withId(R.id.drawerLayoutMain))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(open(Gravity.LEFT))
        DrawerActions.open()

//        onView(withId(R.id.navDrawer)).perform(NavigationUI.)
//            .perform(NavigationViewActions.navigateTo(R.id.destinationApi))



    }


    //todo dodac szuflade
    //todo otwiranie, zamykanie szuflady
    //klikanie na nav buttons
    //klikanie na stzalke wstecz


}