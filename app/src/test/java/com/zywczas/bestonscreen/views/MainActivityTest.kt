package com.zywczas.bestonscreen.views

import android.os.Looper
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zywczas.bestonscreen.R
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivityTest {

    @Rule
    @JvmField
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun isDrawerOpening() {
        onView(withId(R.id.drawerLayoutMain))
            .check(matches(isDisplayed()))
            .check(matches(isClosed()))
            .perform(open(), waitUntilOpen())
        //I tried the below solutions as well, yet the same outcome.
        //Thread.sleep(3000L)
        //Shadows.shadowOf(Looper.getMainLooper()).idle()
        onView(withId(R.id.drawerLayoutMain)).check(matches(isOpen()))
    }

    private fun waitUntilOpen(drawerGravity: Int = GravityCompat.START): ViewAction =
        object : ViewAction {

            private val callback = DrawerOpenCallback()

            override fun getConstraints(): Matcher<View> =
                isAssignableFrom(DrawerLayout::class.java)

            override fun getDescription(): String = "wait for drawer to open"

            override fun perform(uiController: UiController, view: View) {
                if (!(view as DrawerLayout).isDrawerOpen(drawerGravity)) {
                    IdlingRegistry.getInstance().register(callback)
                    view.addDrawerListener(callback)
                    uiController.loopMainThreadUntilIdle()
                    IdlingRegistry.getInstance().unregister(callback)
                    view.removeDrawerListener(callback)
                }
            }
        }

    private class DrawerOpenCallback : IdlingResource, DrawerLayout.DrawerListener {

        private lateinit var callback: IdlingResource.ResourceCallback
        private var opened = false

        override fun getName() = "Drawer open callback"

        override fun isIdleNow() = opened

        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
            this.callback = callback
        }

        override fun onDrawerStateChanged(newState: Int) {
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        }

        override fun onDrawerClosed(drawerView: View) {
        }

        override fun onDrawerOpened(drawerView: View) {
            opened = true
            callback.onTransitionToIdle()
        }
    }


}

//import android.app.Activity
//import android.os.Looper
//import android.view.Gravity
//import android.view.View
//import androidx.appcompat.widget.Toolbar
//import androidx.core.view.GravityCompat
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.test.espresso.Espresso.*
//import androidx.test.espresso.IdlingRegistry
//import androidx.test.espresso.IdlingResource
//import androidx.test.espresso.UiController
//import androidx.test.espresso.ViewAction
//import androidx.test.espresso.action.ViewActions.swipeRight
//import androidx.test.espresso.assertion.ViewAssertions.*
//import androidx.test.espresso.contrib.DrawerActions
//import androidx.test.espresso.contrib.DrawerActions.open
//import androidx.test.espresso.contrib.DrawerMatchers
//import androidx.test.espresso.contrib.DrawerMatchers.isClosed
//import androidx.test.espresso.contrib.DrawerMatchers.isOpen
//import androidx.test.espresso.matcher.ViewMatchers.*
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.zywczas.bestonscreen.R
//import org.hamcrest.Matcher
//import org.hamcrest.core.IsNot.*
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.Shadows.shadowOf
//import org.robolectric.annotation.LooperMode
//
//@RunWith(AndroidJUnit4::class)
//@LooperMode(LooperMode.Mode.PAUSED)
//class MainActivityTest {
//
//    @Rule
//    @JvmField
//    val activity = ActivityScenarioRule(MainActivity::class.java)
//
//    //todo usunac
//    private fun <T : Activity> ActivityScenarioRule<T>.getToolbarNavContentDescription() : String {
//        var description = ""
//        scenario.onActivity {
//            description = it.findViewById<Toolbar>(R.id.toolbarMovies).navigationContentDescription.toString()
//        }
//        return description
//    }
//
//    @Test
//    fun isActivityInView() {
//        onView(withId(R.id.toolbarMovies)).check(matches(isDisplayed()))
//        onView(withId(R.id.drawerLayoutMain)).check(matches(isDisplayed())).check(matches(isClosed()))
//        onView(withId(R.id.navDrawer)).check(matches(not(isDisplayed())))
//    }
//
//    @Test
//    fun isFragmentInContainerInView(){
//        onView(withId(R.id.recyclerViewDB)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun activityDestroyed_isActivityInViewAfterRecreation() {
//        activity.scenario.recreate()
//        onView(withId(R.id.toolbarMovies)).check(matches(isDisplayed()))
//        onView(withId(R.id.drawerLayoutMain)).check(matches(isDisplayed())).check(matches(isClosed()))
//        onView(withId(R.id.navDrawer)).check(matches(not(isDisplayed())))
//        onView(withId(R.id.recyclerViewDB)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun isDrawerOpening(){
//        onView(withId(R.id.drawerLayoutMain))
//            .check(matches(isDisplayed()))
//            .check(matches(isClosed()))
//            .perform(open(), waitUntilOpen())
//        onView(withId(R.id.drawerLayoutMain)).check(matches(isOpen()))
//    }
//
//    private fun waitUntilOpen(drawerGravity: Int = GravityCompat.START): ViewAction = object : ViewAction {
//
//        private val callback = DrawerOpenCallback()
//
//        override fun getConstraints(): Matcher<View> = isAssignableFrom(DrawerLayout::class.java)
//
//        override fun getDescription(): String = "wait for drawer to open"
//
//        override fun perform(uiController: UiController, view: View) {
//            if (!(view as DrawerLayout).isDrawerOpen(drawerGravity)) {
//                IdlingRegistry.getInstance().register(callback)
//                view.addDrawerListener(callback)
//                uiController.loopMainThreadUntilIdle()
//                IdlingRegistry.getInstance().unregister(callback)
//                view.removeDrawerListener(callback)
//            }
//        }
//    }
//
//    private class DrawerOpenCallback : IdlingResource, DrawerLayout.DrawerListener {
//
//        private lateinit var callback: IdlingResource.ResourceCallback
//        private var opened = false
//
//        override fun getName() = "Drawer open callback"
//
//        override fun isIdleNow() = opened
//
//        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
//            this.callback = callback
//        }
//
//        override fun onDrawerStateChanged(newState: Int) {
//        }
//
//        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
//        }
//
//        override fun onDrawerClosed(drawerView: View) {
//        }
//
//        override fun onDrawerOpened(drawerView: View) {
//            opened = true
//            callback.onTransitionToIdle()
//        }
//    }
//
//    @Test
//    fun navigationToDetailsFragment() {
//
//
//
//    }
//
//
//    //todo dodac szuflade
//    //todo otwiranie, zamykanie szuflady
//    //klikanie na nav buttons
//    //klikanie na stzalke wstecz
//
//
//}