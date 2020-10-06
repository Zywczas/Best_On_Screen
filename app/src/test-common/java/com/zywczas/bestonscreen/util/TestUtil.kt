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
        "Genres: Action, Thriller"
    )

    val movieFromDB1 = MovieFromDB(
        724989,
        "/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg",
        "Hard Kill",
        5.5,
        "The work of billionaire tech CEO Donovan Chalmers is so valuable that he hires mercenaries to protect it, and a terrorist group kidnaps his daughter just to get it.",
        "2020-08-25",
        "Genres: Action, Thriller"
    )

    private val movieFromApi2 = MovieFromApi(
        438396, "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        listOf(18, 53),
        "Unknown Origins", 6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims by recreating the first appearance of several comic book superheroes. Cosme, a veteran police inspector who is about to retire, works on the case along with the tormented inspector David Valentín and his own son Jorge Elías, a nerdy young man who owns a comic book store.",
        "2020-08-28"
    )

    val movie2 = Movie(
        438396,
        "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        "Unknown Origins",
        6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims by recreating the first appearance of several comic book superheroes. Cosme, a veteran police inspector who is about to retire, works on the case along with the tormented inspector David Valentín and his own son Jorge Elías, a nerdy young man who owns a comic book store.",
        "2020-08-28",
        "Genres: Drama, Thriller"
    )

    val movieFromDB2 = MovieFromDB(
        438396,
        "/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg",
        "Unknown Origins",
        6.2,
        "In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims by recreating the first appearance of several comic book superheroes. Cosme, a veteran police inspector who is about to retire, works on the case along with the tormented inspector David Valentín and his own son Jorge Elías, a nerdy young man who owns a comic book store.",
        "2020-08-28",
        "Genres: Drama, Thriller"
    )

    val movie3 = Movie(
        13,
        "/7c9UVPPiTPltouxRVY6N9uugaVA.jpg",
        "Forrest Gump",
        8.5,
        "A man with a low IQ has accomplished great things in his life and been present during significant historic events—in each case, far exceeding what anyone imagined he could do. But despite all he has achieved, his one true love eludes him.",
        "1994-07-06",
        "Genres: Comedy, Drama, Romance"
    )

    val movie4 = Movie(
        311,
        "/z1s7ASlypXYGeMLoJ2Np4CpWdzd.jpg",
        "Once Upon a Time in America",
        8.4,
        "A former Prohibition-era Jewish gangster returns to the Lower East Side of Manhattan over thirty years later, where he once again must confront the ghosts and regrets of his old life.",
        "1984-05-23",
        "Genres: Drama, Crime"
    )

    val movie5 = Movie(
        155,
        "/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg",
        "The Dark Knight",
        8.5,
        "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets. The partnership proves to be effective, but they soon find themselves prey to a reign of chaos unleashed by a rising criminal mastermind known to the terrified citizens of Gotham as the Joker.",
        "2008-07-16",
        "Genres: Drama, Action, Crime, Thriller"
    )

    val movie6 = Movie(
        769,
        "/1MWsO3QQd6vB7ENMeXOGil7i0Al.jpg",
        "GoodFellas",
        8.4,
        "The true story of Henry Hill, a half-Irish, half-Sicilian Brooklyn kid who is adopted by neighbourhood gangsters at an early age and climbs the ranks of a Mafia family under the guidance of Jimmy Conway.",
        "1990-09-12",
        "Genres: Crime, Drama"
    )

    val movie7 = Movie(
        664767,
        "/4VlXER3FImHeFuUjBShFamhIp9M.jpg",
        "Mortal Kombat Legends: Scorpion's Revenge",
        8.4,
        "After the vicious slaughter of his family by stone-cold mercenary Sub-Zero, Hanzo Hasashi is exiled to the torturous Netherrealm. There, in exchange for his servitude to the sinister Quan Chi, he’s given a chance to avenge his family – and is resurrected as Scorpion, a lost soul bent on revenge. Back on Earthrealm, Lord Raiden gathers a team of elite warriors – Shaolin monk Liu Kang, Special Forces officer Sonya Blade and action star Johnny Cage – an unlikely band of heroes with one chance to save humanity. To do this, they must defeat Shang Tsung’s horde of Outworld gladiators and reign over the Mortal Kombat tournament.",
        "2020-04-12",
        "Genres: Action, Adventure, Animation, Fantasy"
    )

    val movie8 = Movie(
        550,
        "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
        "Fight Club",
        8.4,
        "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \\\"fight clubs\\\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
        "1999-10-15",
        "Genre: Drama"
    )

    val movie9 = Movie(
        1891,
        "/7BuH8itoSrLExs2YZSsM01Qk2no.jpg",
        "The Empire Strikes Back",
        8.4,
        "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from aging master Yoda. But Darth Vader is more determined than ever to capture Luke. Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO and R2-D2 are thrown into various stages of capture, betrayal and despair.",
        "1980-05-20",
        "Genres: Action, Adventure, Science Fiction"
    )

    val movie10 = Movie(
        423,
        "/enFfoFd3TYs6ttTxrBIfmecQPnz.jpg",
        "The Pianist",
        8.4,
        "The true story of pianist Władysław Szpilman's experiences in Warsaw during the Nazi occupation. When the Jews of the city find themselves forced into a ghetto, Szpilman finds work playing in a café; and when his family is deported in 1942, he stays behind, works for a while as a laborer, and eventually goes into hiding in the ruins of the war-torn city.",
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