package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.NetworkCheck
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.DBVM
import com.zywczas.bestonscreen.viewmodels.DetailsVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNot.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowConnectivityManager

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ApiFragmentTest {

    private val picasso = mockk<Picasso>(relaxed = true)
    private val viewModel = mockk<ApiVM>()
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()
    private val moviesAndCategoryLD = MutableLiveData<Resource<Pair<List<Movie>, Category>>>()
    private val recyclerView = onView(withId(R.id.recyclerViewApi))

    @Before
    fun init(){
        every { viewModelFactory.create(ApiVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns ApiFragment(viewModelFactory, picasso)
        every { viewModel.moviesAndCategoryLD } returns moviesAndCategoryLD
        every { viewModel.getFirstMovies(any()) } answers {
            moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesListOf2, Category.TOP_RATED)) }
        every { viewModel.getNextMoviesIfConnected(any()) } just Runs
    }

    @Test
    fun isFragmentInView() {
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        recyclerView.check(matches(isDisplayed()))
        onView(withId(R.id.progressBarApi)).check(matches(not(isDisplayed())))
        onView(withId(R.id.moviesCategoriesTabs)).check(matches(isDisplayed()))
    }

    @Test
    fun loadingMovies_isProgressBarDisplayed() {
        every { viewModel.getFirstMovies(any()) } just Runs

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

}

//todo ac test sprawdzajacy czy toast sie nie powtarza po recreate
//todo czy wczytuja sie dane
//todo czy sa filmy odpowiednie od razu pokazane
//todo czy po recreate wszystko tak samo poustawiane filmy o zakladki
//todo czy jak sukces to wszytko dobrze wczytane
//todo czy jak error to wszystko ok
//todo czy klikanie na zakladki dziala
//todo czy progress bar sie pokazuje
//todo czy progress bar znika
//todo czy toast sie pokazuje jak error
//todo czy nawigacja dziala
//todo czy recycler view sciaga nowe filmy na przewijanie ekranu