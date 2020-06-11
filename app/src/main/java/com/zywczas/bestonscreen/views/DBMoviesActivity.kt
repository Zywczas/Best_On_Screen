package com.zywczas.bestonscreen.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.adapter.MovieAdapter
import com.zywczas.bestonscreen.viewmodels.DBMoviesVM
import com.zywczas.bestonscreen.viewmodels.MoviesVM
import com.zywczas.bestonscreen.viewmodels.factories.DBMoviesVMFactory
import com.zywczas.bestonscreen.viewmodels.factories.GenericSavedStateViewModelFactory
import com.zywczas.bestonscreen.viewmodels.factories.MoviesVMFactory
import javax.inject.Inject

class DBMoviesActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: DBMoviesVMFactory
    private val dBMoviesVM: DBMoviesVM by viewModels { GenericSavedStateViewModelFactory(factory,this) }
    private lateinit var movieAdapter: MovieAdapter
    @Inject lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

    }

}
