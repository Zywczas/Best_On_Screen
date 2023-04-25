package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.LocalMovie
import com.zywczas.bestonscreen.model.webservice.NetworkMovie
//todo zamienic na extentions fun
fun toMovie(movie: NetworkMovie): Movie = Movie(
    id = movie.id ?: 0,
    posterPath = movie.posterPath ?: "",
    title = movie.title ?: "",
    voteAverage = movie.voteAverage ?: 0.0,
    overview = movie.overview ?: "",
    releaseDate = movie.releaseDate ?: "",
    genresDescription = movie.getGenresDescription()
)

fun toMovie(movie: LocalMovie) = Movie(
    id = movie.id,
    posterPath = movie.posterPath,
    title = movie.title,
    voteAverage = movie.voteAverage,
    overview = movie.overview,
    releaseDate = movie.releaseDate,
    genresDescription = movie.genresDescription
)

fun toLocalMovie(movie: Movie) = LocalMovie(
    id = movie.id,
    posterPath = movie.posterPath,
    title = movie.title,
    voteAverage = movie.voteAverage,
    overview = movie.overview,
    releaseDate = movie.releaseDate,
    genresDescription = movie.genresDescription
)
