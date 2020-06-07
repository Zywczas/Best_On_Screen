package com.zywczas.bestonscreen.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.model.Event
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.localstore.MovieDao
import com.zywczas.bestonscreen.model.localstore.MoviesDataBase
import com.zywczas.bestonscreen.model.webservice.MovieFromApi
import com.zywczas.bestonscreen.model.webservice.TMDBService
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MoviesModule(private val application: Application) {

    @Provides @Singleton
    fun provideCompositeDisposable() : CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideArrayListOfMovies() : ArrayList<Movie> = ArrayList()


    @Provides @Singleton
    fun provideMutableLdOfListOfMovies() : MutableLiveData<List<Movie>> = MutableLiveData()

    @Provides
    fun provideMutableLdOfMovie() : MutableLiveData<Movie> = MutableLiveData()

    @Provides
    fun provideMutableLdOfBoolean() : MutableLiveData<Boolean> = MutableLiveData()

    @Provides
    fun provideBooleanEventLd() : MutableLiveData<Event<Boolean>> = MutableLiveData()

    @Provides
    fun provideIntEventLd() : MutableLiveData<Event<Int>> = MutableLiveData()

    @Provides
    fun provideMovieListEventLd() : MutableLiveData<Event<List<Movie>>> = MutableLiveData()

    @Provides @Singleton
    fun provideTMDBService() : TMDBService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TMDBService::class.java)

    @Provides @Singleton
    fun providePicasso() : Picasso = Picasso.get()

    @Provides @Singleton
    fun provideMovieDao() : MovieDao =
        Room.databaseBuilder(application.applicationContext, MoviesDataBase::class.java, "MoviesDB")
            .build()
            .getMovieDao()

}