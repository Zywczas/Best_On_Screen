package com.zywczas.bestonscreen.di

import androidx.lifecycle.MutableLiveData
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.TMDBService
import com.zywczas.bestonscreen.utilities.BASE_URL
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import javax.inject.Singleton

@Module
class MoviesModule {

    @Provides @Singleton
    fun provideCompositeDisposable() : CompositeDisposable = CompositeDisposable()

    //sprawdzic czy mozna zamienin na collection
    @Provides
    fun provideArrayListOfMovies () : ArrayList<Movie> = ArrayList<Movie>()


    @Provides //sprawdzic czy moze byc singeton - czy zmieniaja sie wyswietlane filmy
    fun provideMutableLiveDataOfListOfMovies () : MutableLiveData<List<Movie>> =
       MutableLiveData<List<Movie>>()

    @Provides @Singleton
    fun provideTMDBService() : TMDBService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TMDBService::class.java)
}