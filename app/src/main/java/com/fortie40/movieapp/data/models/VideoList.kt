package com.fortie40.movieapp.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoList(
    val id: Int = 0,
    val results: List<Video> = arrayListOf()
): Parcelable