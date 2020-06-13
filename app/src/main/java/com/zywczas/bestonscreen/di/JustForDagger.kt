package com.zywczas.bestonscreen.di

import com.zywczas.bestonscreen.adapter.OnBottomReachedListener
import javax.inject.Inject

class JustForDagger @Inject constructor() : OnBottomReachedListener {
    override fun onBottomReached(position: Int) {

    }
}