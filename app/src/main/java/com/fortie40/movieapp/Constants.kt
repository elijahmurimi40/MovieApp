package com.fortie40.movieapp

// Api key for using the TMDb
const val API_KEY = "45955df5a808804d70bf784d00333ec9"

// Base URL for the TMDb API
const val BASE_URL = "https://api.themoviedb.org/3/"

// Base URL for TMDb posters
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

// Base URL for YouTube thumbnails
const val YOUTUBE_THUMBNAILS = "https://img.youtube.com/vi/"

// First page for movies
const val FIRST_PAGE = 1

// Post per page
const val POST_PER_PAGE = 20

// When movie layout
const val MOVIE_VIEW_TYPE = 1

// show loading layout
const val NETWORK_VIEW_TYPE = 2

// Integer Value that number of column in RecyclerView
const val NUMBER_OF_COLUMNS = 2

// Integer Value that number of column in RecyclerView
const val NUMBER_OF_COLUMNS_LAND = 3

// movie id form list to details
const val MOVIE_ID = "movie_id"

// name from main activity to list activity
const val TYPE_OF_MOVIE = "type_of_movie"

// value from main activity to list activity
const val POPULAR = "popular"
const val NOW_PLAYING = "now_playing"
const val UPCOMING = "upcoming"
const val TOP_RATED = "top_rated"

// show movies horizontal
const val MOVIE_VIEW_HORIZONTAL = 2

// show button end list horizontal
const val VIEW_ALL_HORIZONTAL = 1

// id in main activity title
const val ID_TITLE = "id_title"

// main requests to be made
const val NUMBER_OF_REQUEST = 4

// save main array to bundle
const val RESPONSE_ARRAY = "response_array"

// saving scroll position of vertical recyclerview
const val POSITION_INDEX = "position_index"
const val OFFSET = "off_set"

// save recyclerview horizontal scroll position
const val SCROLL_POSITION_HORIZONTAL = "scroll_position_horizontal"

// view model key main activity
const val MAIN_ACTIVITY_KEY = "main_activity_key"

// saving current tab in DetailsActivity
const val CURRENT_PAGE = "current_page"

// saving movie details and list of videos in details activity
const val MOVIE_DETAILS = "movie_details"
const val VIDEO_LIST = "video_list"

// saving tab trailers fragment
const val POSITION_INDEX_TRAILERS = "position_index_trailers"
const val OFFSET_TRAILERS = "offset_trailers"

// intent for trailer activity
// saving and getting from sharedPref TrailerActivity
const val MOVIE_KEY = "movie_key"
const val CURRENT_SECOND = "current_second"
const val MOVIE_TITLE = "movie_title"

// for shared pref
const val SHARED_PREF_FILE = "com.fortie40.movieapp.shared_pref_file"