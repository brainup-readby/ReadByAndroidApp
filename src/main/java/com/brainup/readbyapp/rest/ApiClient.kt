package com.brainup.readbyapp.rest

import com.brainup.readbyapp.com.brainup.readbyapp.rest.HeaderInterceptor
import com.brainup.readbyapp.utils.Constants.BASE_URL
import com.brainup.readbyapp.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {
    /**/
    private var retrofit: Retrofit? = null
    val client: ApiInterface?
        get() {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient().newBuilder()
            httpClient.connectTimeout(120, TimeUnit.SECONDS)
            httpClient.readTimeout(120, TimeUnit.SECONDS)
            httpClient.writeTimeout(120, TimeUnit.SECONDS)
            httpClient.addInterceptor(logging)
            httpClient.addInterceptor(HeaderInterceptor())

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .build()
            }
            return retrofit?.create(ApiInterface::class.java)
        }

}
