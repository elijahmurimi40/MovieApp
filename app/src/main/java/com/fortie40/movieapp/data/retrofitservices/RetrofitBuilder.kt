package com.fortie40.movieapp.data.retrofitservices

import android.annotation.SuppressLint
import android.os.Build
import com.fortie40.movieapp.API_KEY
import com.fortie40.movieapp.BASE_URL
import com.fortie40.movieapp.LATEST_APP_VERSION
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

    private fun tMDBbuilder(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp().build())
    }

    private fun latestVersionBuilder(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl("https://movieappversioncodeandnamephp.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(logger())
                    .build()
            )
    }

    private fun retrofit(classType: String = ""): Retrofit {
        return when(classType) {
            LATEST_APP_VERSION -> latestVersionBuilder().build()
            else -> tMDBbuilder().build()
        }
    }

    fun <S> buildService(serviceType: Class<S>, classType: String = ""): S {
        return retrofit(classType).create(serviceType)
    }
}