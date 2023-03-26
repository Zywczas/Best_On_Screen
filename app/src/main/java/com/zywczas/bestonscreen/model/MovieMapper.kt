package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.LocalMovie
import com.zywczas.bestonscreen.model.webservice.NetworkMovie

fun toMovie(networkMovie: NetworkMovie): Movie {
    networkMovie.convertGenreIdsToDescription()
    return Movie(
        networkMovie.id ?: 0,
        networkMovie.posterPath ?: "",
        networkMovie.title ?: "",
        networkMovie.voteAverage ?: 0.0,
        networkMovie.overview ?: "",
        networkMovie.releaseDate ?: "",
        networkMovie.genresDescription
    )
}

fun toMovie(localMovie: LocalMovie) = Movie(
    localMovie.id,
    localMovie.posterPath,
    localMovie.title,
    localMovie.voteAverage,
    localMovie.overview,
    localMovie.releaseDate,
    localMovie.genresDescription
)

fun toLocalMovie(movie: Movie) = LocalMovie(
    movie.id,
    movie.posterPath,
    movie.title,
    movie.voteAverage,
    movie.overview,
    movie.releaseDate,
    movie.genresDescription
)

