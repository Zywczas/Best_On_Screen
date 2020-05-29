package com.zywczas.bestonscreen.di

import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.TMDBService
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MoviesModule {

    @Provides @Singleton
    fun provideCompositeDisposable() : CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideArrayListOfMovies () : ArrayList<Movie> = ArrayList()


    @Provides //sprawdzic czy moze byc singeton - czy zmieniaja sie wyswietlane filmy
    fun provideMutableLiveDataOfListOfMovies () : MutableLiveData<List<Movie>> = MutableLiveData()

    @Provides
    fun provideMutableLiveDataOfMovie () : MutableLiveData<Movie> = MutableLiveData()

    @Provides @Singleton
    fun provideTMDBService() : TMDBService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TMDBService::class.java)

    @Provides //sprawdzic czy moze byc singleton
    fun providePicasso() : Picasso = Picasso.get()

}