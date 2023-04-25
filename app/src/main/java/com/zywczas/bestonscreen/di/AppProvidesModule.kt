package com.zywczas.bestonscreen.di

import android.app.Application
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MoviesDataBase
import com.zywczas.bestonscreen.model.webservice.MovieRetrofitApi
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppProvidesModule {

    @Provides
    @Singleton
    fun provideMoviesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideMoviesRetrofitApi(retrofit: Retrofit): MovieRetrofitApi = retrofit.create(MovieRetrofitApi::class.java)

    @Provides
    @Singleton
    fun providePicasso(app: Application): Picasso = Picasso.Builder(app.applicationContext).build()

    @Provides
    @Singleton
    fun provideMoviesDataBase(app: Application): MoviesDataBase =
        Room.databaseBuilder(app.applicationContext, MoviesDataBase::class.java, "MoviesDB").build()

    @Provides
    fun provideMovieDao(dataBase: MoviesDataBase): MovieDao = dataBase.getMovieDao()
}
