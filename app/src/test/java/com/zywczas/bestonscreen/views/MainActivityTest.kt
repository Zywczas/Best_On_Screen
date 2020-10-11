package com.zywczas.bestonscreen.views

import android.app.Activity
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerActions.*
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.utilities.CoordinatesClickViewAction
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.core.IsNot.*
import org.junit.Assert.*
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
        onView(withId(R.id.drawerLayoutMain)).check(matches(isDisplayed()))
        onView(withId(R.id.navDrawer)).check(matches(not(isDisplayed())))
    }

    @Test
    fun isFragmentInContainerInView(){
        onView(withId(R.id.recyclerViewDB)).check(matches(isDisplayed()))
    }

    @Test
    fun isDrawerOpening(){
//        clickOnNavButton()
        onView(withId(R.id.drawerLayoutMain)).check(matches(isClosed()))
            .perform(swipeRight()).check(matches(isOpen()))
//        onView(withId(R.id.des)).check(matches(not(isDisplayed())))
//        onView(withContentDescription(activity.getToolbarNavContentDescription())).perform(click())
//        onView(withId(R.id.drawerLayoutMain)).check(matches(isClosed()))
//        onView(withId(R.id.drawerLayoutMain)).check(matches(isOpen(GravityCompat.START)))
//        onView(withContentDescription(activity.getToolbarNavContentDescription())).perform(click())
//        onView(withId(R.id.navDrawer)).check(matches(isDisplayed()))
    }

    private fun clickOnNavButton() = CoordinatesClickViewAction.clickOnCoordinates(50f, 50f)

    private fun clickOutsideDrawer(parentDrawerLayout: View, @LayoutRes drawId: Int) {
        onView(withId(drawId)).perform(
                CoordinatesClickViewAction.clickOnCoordinates(
                    parentDrawerLayout.x + parentDrawerLayout.width + 10,
                    parentDrawerLayout.height / 2f)
            )
    }

    @Test
    fun navigationToDetailsFragment() {
        onView(withId(R.id.drawerLayoutMain))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(open(Gravity.LEFT))

        onView(withId(R.id.navDrawer))
            .perform(NavigationViewActions.navigateTo(R.id.destinationApi))



    }

    //todo czy po recreate dalej dziala
    //todo dodac szuflade
    //todo otwiranie, zamykanie szuflady
    //klikanie na nav buttons
    //klikanie na stzalke wstecz


}