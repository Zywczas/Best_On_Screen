package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Category.POPULAR
import com.zywczas.bestonscreen.model.Category.TOP_RATED
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ApiFragmentTestProgressBarOnly {

    @MockK(relaxUnitFun = true)
    private lateinit var viewModel : ApiVM
    private val picasso = mockk<Picasso>(relaxed = true)
    private val viewModelFactory = mockk<ViewModelsProviderFactory>()
    private val fragmentsFactory = mockk<MoviesFragmentsFactory>()
    private  lateinit var  moviesAndCategoryLD : MutableLiveData<Resource<Pair<List<Movie>, Category>>>
    private val recyclerView = onView(withId(R.id.recyclerViewApi))

    @Before
    fun init(){
        MockKAnnotations.init(this)
        moviesAndCategoryLD = MutableLiveData<Resource<Pair<List<Movie>, Category>>>()
        every { viewModelFactory.create(ApiVM::class.java) } returns viewModel
        every { fragmentsFactory.instantiate(any(), any()) } returns
                ApiFragment(viewModelFactory, picasso)
        every { viewModel.moviesAndCategoryLD } returns moviesAndCategoryLD
    }

    @After
    fun finish(){
        unmockkAll()
    }

    @Test
    fun loadingMoviesOnInit_isProgressBarDisplayed() {
        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun changingCategory_isProgressBarDisplayed() {
        val categorySlot = slot<Category>()
        every { viewModel.getFirstMovies(capture(categorySlot)) } answers
                { moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesList1_2, categorySlot.captured)) }

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        onView(withText("Popular")).perform(click())

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
        verifySequence {
            viewModel.moviesAndCategoryLD
            viewModel.getFirstMovies(TOP_RATED)
            viewModel.getNextMoviesIfConnected(POPULAR)
        }
    }

    @Test
    fun scrollingToBottom_loadingNextPage_isProgressBarDisplayed() {
        val categorySlot = slot<Category>()
        every { viewModel.getFirstMovies(capture(categorySlot)) } answers
                { moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesList1_2, categorySlot.captured)) }

        @Suppress("UNUSED_VARIABLE")
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        recyclerView.perform(swipeUp())

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
        verify (exactly = 1) { viewModel.getNextMoviesIfConnected() }
    }

}