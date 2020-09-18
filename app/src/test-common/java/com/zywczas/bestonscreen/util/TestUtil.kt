package com.zywczas.bestonscreen.util

import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.model.webservice.ApiResponse
import com.zywczas.bestonscreen.model.webservice.MovieFromApi

object TestUtil {

    val movieFromApi1 = MovieFromApi(
        724989,
        "/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg",
        listOf(28, 53),
        "Hard Kill",
        5.5,
        "The work of billionaire tech CEO Donovan Chalmers is so valuable that he hires mercenaries to protect it, and a terrorist group kidnaps his daughter just to get it.",
        "2020-08-25"
    )

    val movie1 = Movie(
        724989,
        "/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg",
        "Hard Kill",
        5.5,
        "The work of billionaire tech CEO Donovan Chalmers is so valuable that he hires mercenaries to protect it, and a terrorist group kidnaps his daughter just to get it.",
        "2020-08-25",
        "Action",
        "Thriller",
        "",
        "",
        "",
        2
    )

    val movieFromDB1 = MovieFromDB(
        724989,
        "/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg",
        "Hard Kill",
        5.5,
        "The work of billionaire tech CEO Donovan Chalmers is so valuable that he hires mercenaries to protect it, and a terrorist group kidnaps his daughter just to get it.",
        "2020-08-25",
        "Action",
        "Thriller",
        "",
        "",
        "",
        2
    )

    private val movieFromApi2 = MovieFromApi(
        438396,
        "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        listOf(18, 53),
        "Unknown Origins",
        6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims...",
        "2020-08-28"
    )

    private val movie2 = Movie(
        438396,
        "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        "Unknown Origins",
        6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims...",
        "2020-08-28",
        "Drama",
        "Thriller",
        "",
        "",
        "",
        2
    )

    val movieFromDB2 = MovieFromDB(
        438396,
        "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        "Unknown Origins",
        6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims...",
        "2020-08-28",
        "Drama",
        "Thriller",
        "",
        "",
        "",
        2
    )

    val movies = listOf(movie1, movie2)
    val moviesFromDb = listOf(movieFromDB1, movieFromDB2)
    private val moviesFromApi = listOf(movieFromApi1, movieFromApi2)
    val apiResponse = ApiResponse(moviesFromApi)


}