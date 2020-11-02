package com.brainup.readbyapp

import android.app.Application
import com.brainup.readbyapp.utils.AppLogger


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLogger.init()
    }
}
