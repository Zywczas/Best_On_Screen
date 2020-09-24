package com.zywczas.bestonscreen.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.BestOnScreenApp
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MoviesDataBase
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.views.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
//todo moze mozna usunac tutaj constructor, czy tak moze byc to application...
@Module
class BestOnScreenModule {

    @Provides
    @AppContext
    fun provideAppContext() : Context = BestOnScreenApp()

    @Provides
    @Singleton
    fun provideTMDBService(): ApiService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    @Provides
    @Singleton
    fun providePicasso(): Picasso = Picasso.Builder(provideAppContext()).build()

    @Provides
    @Singleton
    fun provideMovieDao(): MovieDao =
        Room.databaseBuilder(provideAppContext(), MoviesDataBase::class.java, "MoviesDB")
            .build()
            .getMovieDao()


}