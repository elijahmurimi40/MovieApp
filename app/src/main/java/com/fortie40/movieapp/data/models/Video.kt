package com.fortie40.movieapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val site: String = "",
    val size: Int = 0,
    val type: String = "",

    @SerializedName("iso_639_1")
    val iso6391: String = "",
    @SerializedName("iso_3166_1")
    val iso31661: String = ""
): Parcelable