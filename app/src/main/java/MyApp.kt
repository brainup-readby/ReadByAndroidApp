package com.brainup.readbyapp

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.brainup.readbyapp.utils.AppLogger


class MyApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        AppLogger.init()
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}
