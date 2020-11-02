package com.brainup.readbyapp.com.brainup.readbyapp.utils.extensions

import android.os.Handler
import android.os.Looper


class MainExecutor : ThreadExecutor(Handler(Looper.getMainLooper())) {

    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }
}