package com.zywczas.bestonscreen.utilities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExampleTest {
    //system under test
    private val example = Example()

    @Test
    fun add_isCorrect(){
        //arrange
        val expectedValue = 3

        //act
        val returnedValue = example.add(2, 1)

        //assert
        assertEquals(expectedValue, returnedValue)
    }

}