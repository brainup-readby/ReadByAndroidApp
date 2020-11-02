package com.brainup.readbyapp.com.brainup.readbyapp.rest

import com.brainup.readbyapp.MyApp
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val session = StringBuilder()

        session.append("Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoic2lkIiwiYXV0aG9yaXRpZXMiOlt7ImRlbGVnYXRlIjoib3JnLnNwcmluZ2ZyYW1ld29yay5zZWN1cml0eS5jb3JlLkdyYW50ZWRBdXRob3JpdHkiLCJvd25lciI6Im9yZy5zcHJpbmdmcmFtZXdvcmsuc2VjdXJpdHkuY29yZS5HcmFudGVkQXV0aG9yaXR5IiwidGhpc09iamVjdCI6bnVsbCwicmVzb2x2ZVN0cmF0ZWd5IjowLCJkaXJlY3RpdmUiOjAsInBhcmFtZXRlclR5cGVzIjpbXSwibWF4aW11bU51bWJlck9mUGFyYW1ldGVycyI6MCwibWV0aG9kIjoiZ2V0QXV0aG9yaXR5In1dfQ.2thf6utHk5P1pxVLT_a5U5dzTBeDzsaaQ__C2a2SqlsN8cjBCWQpZgXWfN3W_Ja5zM1cB5FvgS5h93Dp2I4g0A")

        request = request.newBuilder()
            .addHeader("device_id", "525252")
            .addHeader("Authorization", session.toString())
            .build()
        return chain.proceed(request)
    }
}