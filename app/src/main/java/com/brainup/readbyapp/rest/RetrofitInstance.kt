package com.brainup.readbyapp.rest

import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var retrofit: Retrofit? = null
    val service: ApiInterface
        get() {
            if (retrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor(logging)
                retrofit =
                    Retrofit.Builder().baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(LiveDataCallAdapterFactory())
                        .client(httpClient.build())
                        .build()
            }
            return retrofit!!.create(ApiInterface::class.java)
        }
}