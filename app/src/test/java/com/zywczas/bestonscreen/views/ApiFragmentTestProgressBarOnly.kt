package com.zywczas.bestonscreen.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter.*
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Category.*
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.Resource
import com.zywczas.bestonscreen.viewmodels.ApiVM
import com.zywczas.bestonscreen.viewmodels.ViewModelsProviderFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ApiFragmentTestProgressBarOnly {

    //todo zamienic na val
    @MockK(relaxUnitFun = true)
    private lateinit var viewModel : ApiVM
    @RelaxedMockK
    private lateinit var picasso : Picasso
    @MockK
    private lateinit var viewModelFactory : ViewModelsProviderFactory
    @MockK
    private lateinit var fragmentsFactory : MoviesFragmentsFactory
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
    fun loadingMovies_isProgressBarDisplayed() {
        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
    }

    @Test
    fun changingCategory_isProgressBarDisplayed() {
        val categorySlot = slot<Category>()
        every { viewModel.getFirstMovies(capture(categorySlot)) } answers
                { moviesAndCategoryLD.value = Resource.success(Pair(TestUtil.moviesList1_2, categorySlot.captured)) }

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

        val scenario = launchFragmentInContainer<ApiFragment>(factory = fragmentsFactory)
        recyclerView.perform(swipeUp())

        onView(withId(R.id.progressBarApi)).check(matches(isDisplayed()))
        verify (exactly = 1) { viewModel.getNextMoviesIfConnected() }
    }

}