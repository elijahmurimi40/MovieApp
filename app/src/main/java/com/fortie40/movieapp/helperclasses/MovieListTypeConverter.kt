package com.fortie40.movieapp.helperclasses

import androidx.room.TypeConverter
import com.fortie40.movieapp.data.models.Movie
import com.google.gson.Gson

class MovieListTypeConverter {
    @TypeConverter
    fun listToString(value: List<Movie>?) = if (value == null) "" else Gson().toJson(value)

    @TypeConverter
    fun stringToList(value: String?) = if (value == null) arrayListOf() else Gson().fromJson(value, Array<Movie>::class.java).toList()
}