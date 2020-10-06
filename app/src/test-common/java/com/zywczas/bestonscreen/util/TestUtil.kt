package com.zywczas.bestonscreen.util

import com.zywczas.bestonscreen.model.Movie
import com.zywczas.bestonscreen.model.db.MovieFromDB
import com.zywczas.bestonscreen.model.webservice.ApiResponse
import com.zywczas.bestonscreen.model.webservice.MovieFromApi

object TestUtil {

    val movieFromApi1 = MovieFromApi(
        724989,
        "/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg",
        genres = listOf(28, 53),
        "Hard Kill",
        5.5,
        "The work of billionaire tech CEO Donovan Chalmers is...",
        "2020-08-25"
    )

    val movie1 = Movie(
        724989,
        "/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg",
        "Hard Kill",
        5.5,
        "The work of billionaire tech CEO Donovan Chalmers is...",
        "2020-08-25",
        "Genres: Action, Thriller"
    )

    val movieFromDB1 = MovieFromDB(
        724989,
        "/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg",
        "Hard Kill",
        5.5,
        "The work of billionaire tech CEO Donovan Chalmers is...",
        "2020-08-25",
        "Genres: Action, Thriller"
    )

    private val movieFromApi2 = MovieFromApi(
        438396, "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        listOf(18, 53),
        "Unknown Origins", 6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims...",
        "2020-08-28"
    )

    val movie2 = Movie(
        438396,
        "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        "Unknown Origins",
        6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims...",
        "2020-08-28",
        "Genres: Drama, Thriller"
    )

    val movieFromDB2 = MovieFromDB(
        438396,
        "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        "Unknown Origins",
        6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims...",
        "2020-08-28",
        "Genres: Drama, Thriller"
    )

    val movie3 = Movie(
        13,
        "/7c9UVPPiTPltouxRVY6N9uugaVA.jpg",
        "Forrest Gump",
        8.5,
        "A man with a low IQ has accomplished great things...",
        "1994-07-06",
        "Genres: Comedy, Drama, Romance"
    )

    val movie4 = Movie(
        311,
        "/z1s7ASlypXYGeMLoJ2Np4CpWdzd.jpg",
        "Once Upon a Time in America",
        8.4,
        "A man with a low IQ has accomplished great things...",
        "1984-05-23",
        "Genres: Drama, Crime"
    )

    val movie5 = Movie(
        155,
        "/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg",
        "The Dark Knight",
        8.5,
        "Batman raises the stakes in his war on crime.",
        "2008-07-16",
        "Genres: Drama, Action, Crime, Thriller"
    )

    val movie6 = Movie(
        769,
        "/1MWsO3QQd6vB7ENMeXOGil7i0Al.jpg",
        "GoodFellas",
        8.4,
        "The true story of Henry Hill.",
        "1990-09-12",
        "Genres: Crime, Drama"
    )

    val movie7 = Movie(
        664767,
        "/4VlXER3FImHeFuUjBShFamhIp9M.jpg",
        "Mortal Kombat Legends: Scorpion's Revenge",
        8.4,
        "After the vicious slaughter of his family...",
        "2020-04-12",
        "Genres: Action, Adventure, Animation, Fantasy"
    )

    val movie8 = Movie(
        550,
        "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
        "Fight Club",
        8.4,
        "A ticking-time-bomb insomniac and a slippery soap salesman.",
        "1999-10-15",
        "Genre: Drama"
    )

    val movie9 = Movie(
        1891,
        "/7BuH8itoSrLExs2YZSsM01Qk2no.jpg",
        "The Empire Strikes Back",
        8.4,
        "The epic saga continues as Luke Skywalker...",
        "1980-05-20",
        "Genres: Action, Adventure, Science Fiction"
    )

    val movie10 = Movie(
        423,
        "/enFfoFd3TYs6ttTxrBIfmecQPnz.jpg",
        "The Pianist",
        8.4,
        "The true story of pianist Władysław Szpilman.",
        "2002-09-17",
        "Genres: Drama, War"
    )

    val moviesListOf2 = listOf(movie1, movie2)
    val moviesFromDb = listOf(movieFromDB1, movieFromDB2)
    private val moviesFromApi = listOf(movieFromApi1, movieFromApi2)
    val apiResponse = ApiResponse(moviesFromApi)
    val moviesListOf10 = listOf(movie1, movie2, movie3, movie4, movie5, movie6, movie7,
    movie8, movie9, movie10)


}