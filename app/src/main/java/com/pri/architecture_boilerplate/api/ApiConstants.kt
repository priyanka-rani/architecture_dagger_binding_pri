package com.pri.architecture_boilerplate.api

import com.pri.architecture_boilerplate.BuildConfig

object ApiEndPoint {
    /* Api related to Authentication */
    const val ENDPOINT_GET_MOVIE_LIST = "movie/{path}?api_key=4bc53930f5725a4838bf943d02af6aa9&language=en-US"
    const val ENDPOINT_SEARCH_MOVIE_LIST = "search/movie?api_key=4bc53930f5725a4838bf943d02af6aa9&language=en-US"
}

object ResponseCodes {
    const val CODE_SUCCESS = 200
    const val CODE_TOKEN_EXPIRE = 401
    const val CODE_UNAUTHORIZED = 403
    const val CODE_VALIDATION_ERROR = 400
    const val CODE_DEVICE_CHANGE = 451
}


const val API_URL = "https://api.themoviedb.org/3/"


const val TOP_RATED = "top_rated"
const val NOW_PLAYING = "now_playing"
const val UPCOMING = "upcoming"
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185"
