package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.model.webservice.MovieFromApi

fun toMovie(movieFromApi: MovieFromApi) = Movie(
    movieFromApi.id ?: 0,
    movieFromApi.posterPath ?: "",
    movieFromApi.title ?: "",
    movieFromApi.voteAverage ?: 0.0,
    movieFromApi.overview ?: "",
    movieFromApi.releaseDate ?: "",
    movieFromApi.genre1,
    movieFromApi.genre2,
    movieFromApi.genre3,
    movieFromApi.genre4,
    movieFromApi.genre5,
    movieFromApi.assignedGenresAmount
)

fun toMovie(movieFromDB: MovieFromDB) = Movie(
    movieFromDB.id,
    movieFromDB.posterPath,
    movieFromDB.title,
    movieFromDB.voteAverage,
    movieFromDB.overview,
    movieFromDB.releaseDate,
    movieFromDB.genre1,
    movieFromDB.genre2,
    movieFromDB.genre3,
    movieFromDB.genre4,
    movieFromDB.genre5,
    movieFromDB.assignedGenresAmount
)

fun toMovieFromDB(movie: Movie) = MovieFromDB(
    movie.id,
    movie.posterPath,
    movie.title,
    movie.voteAverage,
    movie.overview,
    movie.releaseDate,
    movie.genre1,
    movie.genre2,
    movie.genre3,
    movie.genre4,
    movie.genre5,
    movie.assignedGenresAmount
)

