package com.zywczas.bestonscreen.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.RequestHandler
import com.zywczas.bestonscreen.BestOnScreenApp
import com.zywczas.bestonscreen.R
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

@Module
class BestOnScreenModule {

    companion object {

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
        fun providePicasso(app: Application): Picasso = Picasso.Builder(app.applicationContext).build()

        @Provides
        @Singleton
        fun provideMovieDao(app: Application): MovieDao =
            Room.databaseBuilder(app.applicationContext, MoviesDataBase::class.java, "MoviesDB")
                .build()
                .getMovieDao()

    }


}