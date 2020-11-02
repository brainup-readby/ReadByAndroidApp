/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brainup.readbyapp.rest

import android.util.ArrayMap
import android.util.Log
import com.brainup.readbyapp.utils.Constants
import org.json.JSONObject
import retrofit2.Response
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
class ApiResponse<T> {
    val code: Int
    val body: T?
    val errorMessage: String?
    val links: MutableMap<String, String>

    constructor(error: Throwable) {
        code = 100
        body = null
        errorMessage = error.message
        links = ArrayMap<String, String>()
    }

    constructor(response: Response<T>) {
        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            errorMessage = null
        } else {
            var message: String? = null
            body = response.body()
            if (response.errorBody() != null) {
                val jObjError = JSONObject(response.errorBody()!!.string())
                if (jObjError.has(Constants.KEY_MESSAGE)) {
                    message = jObjError.getString(Constants.KEY_MESSAGE)
                }
            }

            /*  if (response.errorBody() != null) {
                  try {
                      message = response.errorBody()!!.string()
                  } catch (ignored: IOException) {
                      Log.e("Parse", "error while parsing response")
                  }

              }
              if (message == null || message.trim { it <= ' ' }.isEmpty()) {
                  message = response.message()
              }*/
            errorMessage = message
        }
        val linkHeader = response.headers().get("link")
        if (linkHeader == null) {
            links = ArrayMap<String, String>()
        } else {
            links = ArrayMap<String, String>()
            val matcher = LINK_PATTERN.matcher(linkHeader)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
        }
    }

    val isSuccessful: Boolean
        get() = code in 200..299

    val nextPage: Int?
        get() {
            val next = links[NEXT_LINK] ?: return null
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() != 1) {
                return null
            }
            try {
                return Integer.parseInt(matcher.group(1))
            } catch (ex: NumberFormatException) {
                Log.e("Parse", "cannot parse next page from " + next)
                return null
            }

        }

    companion object {
        private val LINK_PATTERN = Pattern
            .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("page=(\\d)+")
        private val NEXT_LINK = "next"
    }
}