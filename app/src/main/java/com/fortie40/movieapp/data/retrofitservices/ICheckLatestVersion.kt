package com.fortie40.movieapp.data.retrofitservices

import com.fortie40.movieapp.data.models.CheckLatestVersion
import retrofit2.Call
import retrofit2.http.POST

interface ICheckLatestVersion {
    @POST("get")
    fun getLatestVersion(): Call<CheckLatestVersion>
}