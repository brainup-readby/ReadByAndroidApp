package com.brainup.readbyapp.com.brainup.readbyapp.rest

import com.brainup.readbyapp.MyApp
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val session = StringBuilder()

        session.append("Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoicmVhZCIsImF1dGhvcml0aWVzIjpbeyJkZWxlZ2F0ZSI6Im9yZy5zcHJpbmdmcmFtZXdvcmsuc2VjdXJpdHkuY29yZS5HcmFudGVkQXV0aG9yaXR5Iiwib3duZXIiOiJvcmcuc3ByaW5nZnJhbWV3b3JrLnNlY3VyaXR5LmNvcmUuR3JhbnRlZEF1dGhvcml0eSIsInRoaXNPYmplY3QiOm51bGwsInJlc29sdmVTdHJhdGVneSI6MCwiZGlyZWN0aXZlIjowLCJwYXJhbWV0ZXJUeXBlcyI6W10sIm1heGltdW1OdW1iZXJPZlBhcmFtZXRlcnMiOjAsIm1ldGhvZCI6ImdldEF1dGhvcml0eSJ9XX0.nOgf_twKN_lhmHMIl1reTwrNN71QiAaeyu9iB5ytRv21gpvU4PR7v9JI9X2vNYZ9TETrsxCRLe3GaezJbGgyig")

        request = request.newBuilder()
            .addHeader("device_id", "1234567890")
            .addHeader("Authorization", session.toString())
            .build()
        return chain.proceed(request)
    }
}