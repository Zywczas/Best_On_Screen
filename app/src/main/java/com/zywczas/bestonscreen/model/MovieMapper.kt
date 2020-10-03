package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.model.webservice.MovieFromApi

fun toMovie(movieFromApi: MovieFromApi): Movie {
    return Movie(
        movieFromApi.id ?: 0,
        movieFromApi.posterPath ?: "",
        movieFromApi.title ?: "",
        movieFromApi.voteAverage ?: 0.0,
        movieFromApi.overview ?: "",
        movieFromApi.releaseDate ?: "",
        movieFromApi.genresDescription
    )
}

fun toMovie(movieFromDB: MovieFromDB) = Movie(
    movieFromDB.id,
    movieFromDB.posterPath,
    movieFromDB.title,
    movieFromDB.voteAverage,
    movieFromDB.overview,
    movieFromDB.releaseDate,
    movieFromDB.genresDescription
)

fun toMovieFromDB(movie: Movie) = MovieFromDB(
    movie.id,
    movie.posterPath,
    movie.title,
    movie.voteAverage,
    movie.overview,
    movie.releaseDate,
    movie.genresDescription
)

