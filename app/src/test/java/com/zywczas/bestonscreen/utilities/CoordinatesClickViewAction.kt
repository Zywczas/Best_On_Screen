package com.zywczas.bestonscreen.utilities

import android.view.InputDevice
import android.view.MotionEvent
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap

//todo zamienic na object
class CoordinatesClickViewAction {
    companion object {
        fun clickOnCoordinates(x: Float, y: Float): ViewAction {
            return GeneralClickAction(Tap.SINGLE, CoordinatesProvider { view ->
                val screenPos = IntArray(2)
                view.getLocationOnScreen(screenPos)
                val screenX = screenPos[0] + x
                val screenY = screenPos[1] + y
                floatArrayOf(screenX, screenY)
            }, Press.FINGER, InputDevice.SOURCE_ANY, MotionEvent.BUTTON_PRIMARY)
        }
    }
}