package com.zywczas.bestonscreen.viewmodels

import com.zywczas.bestonscreen.model.ApiRepository
import com.zywczas.bestonscreen.InstantExecutorExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock

@ExtendWith(InstantExecutorExtension::class)
internal class ApiVMTest{

    //system under test
    lateinit var viewModel : ApiVM

    @Mock
    lateinit var repo : ApiRepository


    @BeforeEach
    private fun init(){

    }
}