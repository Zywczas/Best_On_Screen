package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.DetailsRepository
import org.junit.jupiter.api.Assertions.*

import com.zywczas.bestonscreen.util.LiveDataTestUtil
import com.zywczas.bestonscreen.util.TestUtil
import com.zywczas.bestonscreen.utilities.InstantExecutorExtension
import io.reactivex.rxjava3.core.Flowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
internal class DetailsVMTest {

    //system under test
    private lateinit var viewModel : DetailsVM

    @Mock
    private lateinit var repo : DetailsRepository

}