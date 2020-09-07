package com.zywczas.bestonscreen.di

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.model.Category
import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.db.MovieDao
import com.zywczas.bestonscreen.model.db.MoviesDataBase
import com.zywczas.bestonscreen.model.webservice.ApiService
import com.zywczas.bestonscreen.utilities.Event
import com.zywczas.bestonscreen.utilities.Resource
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class MoviesModule(private val application: Application) {

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    fun provideArrayListOfMovies() = ArrayList<Movie>()

    @Provides
    fun provideStringEventLd() = MutableLiveData<Event<String>>()

    @Provides
    fun provideStringEventMediatorLiveData() = MediatorLiveData<Event<String>>()

    @Provides
    fun provideMutableLdTripleMoviesIntCategory() = MutableLiveData<Triple<List<Movie>, Int, Category>>()

    @Provides
    fun provideMediatorLdTripleMoviesIntCategory() = MediatorLiveData<Triple<List<Movie>, Int, Category>>()

    @Provides
    fun provideMediatorLdPairMoviesCategory() = MediatorLiveData<Pair<List<Movie>, Category>>()

    @Provides
    fun provideMediatorLdResourcePairMoviesCategory() = MediatorLiveData<Resource<Pair<List<Movie>, Category>>>()


    @Provides
    fun provideMoviesMutableLiveData() = MutableLiveData<List<Movie>>()

    //todo sprawdzic co tu usunac
    @Provides
    fun provideBooleanMutableLiveData() = MutableLiveData<Boolean>()

    @Provides
    fun provideBooleanMediatorLiveData() = MediatorLiveData<Boolean>()
    //todo posprawdzac czy nazwy sa w tym samym szyku

    @Provides
    fun provideBooleanResourceEventMediatorLiveData() = MediatorLiveData<Event<Resource<Boolean>>>()

    @Provides @Singleton
    fun provideTMDBService() : ApiService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    @Provides @Singleton
    fun providePicasso() : Picasso = Picasso.get()
 
    @Provides @Singleton
    fun provideMovieDao() : MovieDao =
        Room.databaseBuilder(application.applicationContext, MoviesDataBase::class.java, "MoviesDB")
            .build()
            .getMovieDao()

}