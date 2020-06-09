package com.zywczas.bestonscreen.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zywczas.bestonscreen.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun showsBtnMainClicked(view: View) {
        val showsActivity = Intent(this, ShowsActivity::class.java)
        startActivity(showsActivity)
    }

    fun movieBtnMainClicked(view: View) {
        val moviesActivity = Intent(this, MoviesActivity::class.java)
        startActivity(moviesActivity)
    }
}
