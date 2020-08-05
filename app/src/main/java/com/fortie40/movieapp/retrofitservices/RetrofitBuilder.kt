package com.fortie40.movieapp.retrofitservices

import android.annotation.SuppressLint
import android.os.Build
import com.fortie40.movieapp.API_KEY
import com.fortie40.movieapp.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object RetrofitBuilder {
    // create Logger
    private fun logger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @SuppressLint("ConstantLocale")
    private val requestInterceptor = Interceptor { chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("language", Locale.getDefault().toString())
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return@Interceptor chain.proceed(request)
    }

    // Create OkHttp Client
    private fun okHttp(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor {
                var request = it.request()

                request = request.newBuilder()
                    .addHeader("x-device-type", Build.DEVICE)
                    .addHeader("Accept-Language", Locale.getDefault().language)
                    .build()

                it.proceed(request)
            }
            .addInterceptor(logger())
    }

    private fun builder(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp().build())
    }

    private fun retrofit(): Retrofit {
        return builder().build()
    }

    fun <S> buildService(serviceType: Class<S>): S {
        return retrofit().create(serviceType)
    }
}