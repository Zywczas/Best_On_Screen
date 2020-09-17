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

    const val restApiResponseBody = "{\n" +
            "  \"page\": 1,\n" +
            "  \"total_results\": 10000,\n" +
            "  \"total_pages\": 500,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"popularity\": 1773.016,\n" +
            "      \"vote_count\": 1582,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/aKx1ARwG55zZ0GpRvU2WrGrCG9o.jpg\",\n" +
            "      \"id\": 337401,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/zzWGRw277MNoCs3zhyG3YmYQsXv.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Mulan\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        18,\n" +
            "        14,\n" +
            "        10752\n" +
            "      ],\n" +
            "      \"title\": \"Mulan\",\n" +
            "      \"vote_average\": 7.6,\n" +
            "      \"overview\": \"When the Emperor of China issues a decree that one man per family must serve in the Imperial Chinese Army to defend the country from Huns, Hua Mulan, the eldest daughter of an honored warrior, steps in to take the place of her ailing father. She is spirited, determined and quick on her feet. Disguised as a man by the name of Hua Jun, she is tested every step of the way and must harness her innermost strength and embrace her true potential.\",\n" +
            "      \"release_date\": \"2020-09-10\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 1311.544,\n" +
            "      \"vote_count\": 0,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/6CoRTJTmijhBLJTUNoVSUNxZMEI.jpg\",\n" +
            "      \"id\": 694919,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/pq0JSpwyT2URytdFG0euztQPAyR.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Money Plane\",\n" +
            "      \"genre_ids\": [\n" +
            "        28\n" +
            "      ],\n" +
            "      \"title\": \"Money Plane\",\n" +
            "      \"vote_average\": 0,\n" +
            "      \"overview\": \"A professional thief with \$40 million in debt and his family's life on the line must commit one final heist - rob a futuristic airborne casino filled with the world's most dangerous criminals.\",\n" +
            "      \"release_date\": \"2020-09-29\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 1024.129,\n" +
            "      \"vote_count\": 46,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/ugZW8ocsrfgI95pnQ7wrmKDxIe.jpg\",\n" +
            "      \"id\": 724989,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/86L8wqGMDbwURPni2t7FQ0nDjsH.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Hard Kill\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"Hard Kill\",\n" +
            "      \"vote_average\": 5.5,\n" +
            "      \"overview\": \"The work of billionaire tech CEO Donovan Chalmers is so valuable that he hires mercenaries to protect it, and a terrorist group kidnaps his daughter just to get it.\",\n" +
            "      \"release_date\": \"2020-08-25\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 934.316,\n" +
            "      \"vote_count\": 54,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/9Rj8l6gElLpRL7Kj17iZhrT5Zuw.jpg\",\n" +
            "      \"id\": 734309,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/7fvdg211A2L0mHddvzyArRuRalp.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Santana\",\n" +
            "      \"genre_ids\": [\n" +
            "        28\n" +
            "      ],\n" +
            "      \"title\": \"Santana\",\n" +
            "      \"vote_average\": 5.8,\n" +
            "      \"overview\": \"Two brothers — one a narcotics agent and the other a general — finally discover the identity of the drug lord who murdered their parents decades ago. They may kill each other before capturing the bad guys.\",\n" +
            "      \"release_date\": \"2020-08-28\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 816.32,\n" +
            "      \"vote_count\": 206,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/uOw5JD8IlD546feZ6oxbIjvN66P.jpg\",\n" +
            "      \"id\": 718444,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/x4UkhIQuHIJyeeOTdcbZ3t3gBSa.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Rogue\",\n" +
            "      \"genre_ids\": [\n" +
            "        28\n" +
            "      ],\n" +
            "      \"title\": \"Rogue\",\n" +
            "      \"vote_average\": 5.9,\n" +
            "      \"overview\": \"Battle-hardened O’Hara leads a lively mercenary team of soldiers on a daring mission: rescue hostages from their captors in remote Africa. But as the mission goes awry and the team is stranded, O’Hara’s squad must face a bloody, brutal encounter with a gang of rebels.\",\n" +
            "      \"release_date\": \"2020-08-20\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 830.228,\n" +
            "      \"vote_count\": 137,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/sMO1v5TUf8GOJHbJieDXsgWT2Ud.jpg\",\n" +
            "      \"id\": 438396,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/qGZe9qTuydxyJYQ60XDtEckzLR8.jpg\",\n" +
            "      \"original_language\": \"es\",\n" +
            "      \"original_title\": \"Orígenes secretos\",\n" +
            "      \"genre_ids\": [\n" +
            "        18,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"Unknown Origins\",\n" +
            "      \"vote_average\": 6.3,\n" +
            "      \"overview\": \"In Madrid, Spain, a mysterious serial killer ruthlessly murders his victims by recreating the first appearance of several comic book superheroes. Cosme, a veteran police inspector who is about to retire, works on the case along with the tormented inspector David Valentín and his own son Jorge Elías, a nerdy young man who owns a comic book store.\",\n" +
            "      \"release_date\": \"2020-08-28\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 786.374,\n" +
            "      \"vote_count\": 436,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/sy6DvAu72kjoseZEjocnm2ZZ09i.jpg\",\n" +
            "      \"id\": 581392,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/gEjNlhZhyHeto6Fy5wWy5Uk3A9D.jpg\",\n" +
            "      \"original_language\": \"ko\",\n" +
            "      \"original_title\": \"반도\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        27,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"Peninsula\",\n" +
            "      \"vote_average\": 7.2,\n" +
            "      \"overview\": \"A soldier and his team battle hordes of post-apocalyptic zombies in the wastelands of the Korean Peninsula.\",\n" +
            "      \"release_date\": \"2020-07-15\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 617.823,\n" +
            "      \"vote_count\": 1204,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/TnOeov4w0sTtV2gqICqIxVi74V.jpg\",\n" +
            "      \"id\": 605116,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/qVygtf2vU15L2yKS4Ke44U4oMdD.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Project Power\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        80,\n" +
            "        878\n" +
            "      ],\n" +
            "      \"title\": \"Project Power\",\n" +
            "      \"vote_average\": 6.7,\n" +
            "      \"overview\": \"An ex-soldier, a teen and a cop collide in New Orleans as they hunt for the source behind a dangerous new pill that grants users temporary superpowers.\",\n" +
            "      \"release_date\": \"2020-08-14\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 548.628,\n" +
            "      \"vote_count\": 63,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/n6hptKS7Y0ZjkYwbqKOK3jz9XAC.jpg\",\n" +
            "      \"id\": 594328,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/lkeBhXGJFRlhI7cBWn8LQQAdZqK.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Phineas and Ferb  The Movie Candace Against the Universe\",\n" +
            "      \"genre_ids\": [\n" +
            "        16,\n" +
            "        35,\n" +
            "        10402,\n" +
            "        878,\n" +
            "        10751,\n" +
            "        10770\n" +
            "      ],\n" +
            "      \"title\": \"Phineas and Ferb  The Movie Candace Against the Universe\",\n" +
            "      \"vote_average\": 7,\n" +
            "      \"overview\": \"Phineas and Ferb travel across the galaxy to rescue their older sister Candace, who has been abducted by aliens and taken to a utopia in a far-off planet, free of her pesky little brothers.\",\n" +
            "      \"release_date\": \"2020-08-28\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 534.745,\n" +
            "      \"vote_count\": 269,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/kiX7UYfOpYrMFSAGbI6j1pFkLzQ.jpg\",\n" +
            "      \"id\": 613504,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/r5srC0cqU36n38azFnCyReEksiR.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"After We Collided\",\n" +
            "      \"genre_ids\": [\n" +
            "        18,\n" +
            "        10749\n" +
            "      ],\n" +
            "      \"title\": \"After We Collided\",\n" +
            "      \"vote_average\": 6.9,\n" +
            "      \"overview\": \"Tessa finds herself struggling with her complicated relationship with Hardin; she faces a dilemma that could change their lives forever.\",\n" +
            "      \"release_date\": \"2020-09-02\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 480.244,\n" +
            "      \"vote_count\": 75,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/i4kPwXPlM1iy8Jf3S1uuLuwqQAV.jpg\",\n" +
            "      \"id\": 721452,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/riDrpqQtZpXGeiJdlmfcwwPH7nN.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"One Night in Bangkok\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"One Night in Bangkok\",\n" +
            "      \"vote_average\": 7.2,\n" +
            "      \"overview\": \"A hit man named Kai flies into Bangkok, gets a gun, and orders a cab. He offers a professional female driver big money to be his all-night driver. But when she realizes Kai is committing brutal murders at each stop, it's too late to walk away. Meanwhile, an offbeat police detective races to decode the string of slayings before more blood is spilled.\",\n" +
            "      \"release_date\": \"2020-08-25\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 473.508,\n" +
            "      \"vote_count\": 271,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/uGhQ2ZGBpzCj6wC5jUrybsZuPTI.jpg\",\n" +
            "      \"id\": 539885,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/dbOqWLNWg1Xphshyus0UMSN8fFP.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Ava\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        80,\n" +
            "        18,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"Ava\",\n" +
            "      \"vote_average\": 5.9,\n" +
            "      \"overview\": \"A black ops assassin is forced to fight for her own survival after a job goes dangerously wrong.\",\n" +
            "      \"release_date\": \"2020-08-06\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 445.55,\n" +
            "      \"vote_count\": 75,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/eDnHgozW8vfOaLHzfpHluf1GZCW.jpg\",\n" +
            "      \"id\": 606234,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/u9YEh2xVAPVTKoaMNlB5tH6pXkm.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Archive\",\n" +
            "      \"genre_ids\": [\n" +
            "        878\n" +
            "      ],\n" +
            "      \"title\": \"Archive\",\n" +
            "      \"vote_average\": 6.1,\n" +
            "      \"overview\": \"2038: George Almore is working on a true human-equivalent AI, and his latest prototype is almost ready. This sensitive phase is also the riskiest as he has a goal that must be hidden at all costs—being reunited with his dead wife.\",\n" +
            "      \"release_date\": \"2020-08-13\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 427.497,\n" +
            "      \"vote_count\": 252,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/imy1OoT1xddt2kqw6hhc4v01e8i.jpg\",\n" +
            "      \"id\": 623491,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/naUJGskMlDg2HZ0FhsaMG5xPF2K.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"The Babysitter: Killer Queen\",\n" +
            "      \"genre_ids\": [\n" +
            "        35,\n" +
            "        27\n" +
            "      ],\n" +
            "      \"title\": \"The Babysitter: Killer Queen\",\n" +
            "      \"vote_average\": 6.7,\n" +
            "      \"overview\": \"Two years after defeating a satanic cult led by his babysitter Bee, Cole's trying to forget his past and focus on surviving high school. But when old enemies unexpectedly return, Cole will once again have to outsmart the forces of evil.\",\n" +
            "      \"release_date\": \"2020-09-10\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 374.35,\n" +
            "      \"vote_count\": 105,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/lv3RonWge4GlC9ymNzC0oWpFCfv.jpg\",\n" +
            "      \"id\": 611395,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/qXACJOuyklS0BpvO8ALLkkrsv7W.jpg\",\n" +
            "      \"original_language\": \"zh\",\n" +
            "      \"original_title\": \"征途\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        12,\n" +
            "        14\n" +
            "      ],\n" +
            "      \"title\": \"Double World\",\n" +
            "      \"vote_average\": 6.9,\n" +
            "      \"overview\": \"Keen to bring honor to his clan, young villager Dong Yilong embarks on a perilous journey to compete in a tournament that selects warriors for battle.\",\n" +
            "      \"release_date\": \"2020-07-24\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 395.603,\n" +
            "      \"vote_count\": 144,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/aVbqhqYtlxwEGihTEhewZAgDOCX.jpg\",\n" +
            "      \"id\": 489326,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/dFB6Tiy3z2xRLbnEUB5ocApT5xG.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Mortal\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        14,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"Mortal\",\n" +
            "      \"vote_average\": 6.8,\n" +
            "      \"overview\": \"A young boy must discover the origins of his extraordinary powers before he is captured by authorities hell-bent on condemning him for an accidental murder.\",\n" +
            "      \"release_date\": \"2020-02-28\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 361.329,\n" +
            "      \"vote_count\": 86,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/sDi6wKgECUjDug2gn4uODSqZ3yC.jpg\",\n" +
            "      \"id\": 632618,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/cVdYaAQmd5DZNdo0KFJruz7JpUs.jpg\",\n" +
            "      \"original_language\": \"es\",\n" +
            "      \"original_title\": \"Crímenes de familia\",\n" +
            "      \"genre_ids\": [\n" +
            "        80,\n" +
            "        18,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"The Crimes That Bind\",\n" +
            "      \"vote_average\": 6.8,\n" +
            "      \"overview\": \"When her son is accused of raping and trying to murder his ex-wife, Alicia embarks on a journey that will change her life forever.\",\n" +
            "      \"release_date\": \"2020-08-20\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 347.775,\n" +
            "      \"vote_count\": 30,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/q2lkJf1TAjImTHCEO7XvbqPtnPb.jpg\",\n" +
            "      \"id\": 703134,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/j57oUw8LIYvjOl0zs3A1A1UqwKH.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Infamous\",\n" +
            "      \"genre_ids\": [\n" +
            "        80,\n" +
            "        18,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"Infamous\",\n" +
            "      \"vote_average\": 5.3,\n" +
            "      \"overview\": \"Two young lovers rob their way across the southland, posting their exploits to social media, and gaining fame and followers as a result.\",\n" +
            "      \"release_date\": \"2020-06-12\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 360.186,\n" +
            "      \"vote_count\": 5157,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/y95lQLnuNKdPAzw9F9Ab8kJ80c3.jpg\",\n" +
            "      \"id\": 38700,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/upUy2QhMZEmtypPW3PdieKLAHxh.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"Bad Boys for Life\",\n" +
            "      \"genre_ids\": [\n" +
            "        28,\n" +
            "        80,\n" +
            "        53\n" +
            "      ],\n" +
            "      \"title\": \"Bad Boys for Life\",\n" +
            "      \"vote_average\": 7.3,\n" +
            "      \"overview\": \"Marcus and Mike are forced to confront new threats, career changes, and midlife crises as they join the newly created elite team AMMO of the Miami police department to take down the ruthless Armando Armas, the vicious leader of a Miami drug cartel.\",\n" +
            "      \"release_date\": \"2020-01-15\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"popularity\": 414.424,\n" +
            "      \"vote_count\": 391,\n" +
            "      \"video\": false,\n" +
            "      \"poster_path\": \"/kPzcvxBwt7kEISB9O4jJEuBn72t.jpg\",\n" +
            "      \"id\": 677638,\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/pO1SnM5a1fEsYrFaVZW78Wb0zRJ.jpg\",\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_title\": \"We Bare Bears: The Movie\",\n" +
            "      \"genre_ids\": [\n" +
            "        12,\n" +
            "        16,\n" +
            "        35,\n" +
            "        10751\n" +
            "      ],\n" +
            "      \"title\": \"We Bare Bears: The Movie\",\n" +
            "      \"vote_average\": 7.8,\n" +
            "      \"overview\": \"When Grizz, Panda, and Ice Bear's love of food trucks and viral videos went out of hand, it catches the attention of Agent Trout from the National Wildlife Control, who pledges to restore the “natural order” by separating them forever. Chased away from their home, the Bears embark on an epic road trip as they seek refuge in Canada, with their journey being filled with new friends, perilous obstacles, and huge parties. The risky journey also forces the Bears to face how they first met and became brothers, in order to keep their family bond from splitting apart.\",\n" +
            "      \"release_date\": \"2020-06-30\"\n" +
            "    }\n" +
            "  ]\n" +
            "}"
}