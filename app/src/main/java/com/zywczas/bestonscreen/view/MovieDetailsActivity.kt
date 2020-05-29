package com.zywczas.bestonscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.di.App
import com.zywczas.bestonscreen.viewModel.MovieDetailsVM
import com.zywczas.bestonscreen.viewModel.MovieDetailsVMFactory
import javax.inject.Inject

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var movieDetailsVM: MovieDetailsVM
    @Inject lateinit var factory: MovieDetailsVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        App.moviesComponent.inject(this)

        movieDetailsVM = ViewModelProvider(this, factory).get(MovieDetailsVM::class.java)


    }




    override fun onDestroy() {
        movieDetailsVM.clear()
        super.onDestroy()
    }
}
