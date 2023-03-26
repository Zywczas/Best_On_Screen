package com.zywczas.bestonscreen.di

import android.app.Application
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MoviesDataBase
import com.zywczas.bestonscreen.model.webservice.NetworkMovieService
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object BestOnScreenModule {

    @Provides
    fun provideTMDBService(): NetworkMovieService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NetworkMovieService::class.java)

    @Provides
    @Singleton
    fun providePicasso(app: Application): Picasso = Picasso.Builder(app.applicationContext).build()

    @Provides
    @Singleton
    fun provideMovieDao(app: Application): MovieDao =
        Room.databaseBuilder(app.applicationContext, MoviesDataBase::class.java, "MoviesDB")
            .build()
            .getMovieDao()

}
