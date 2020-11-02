package com.brainup.readbyapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LoginActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.DashBoardActivity
import com.brainup.readbyapp.utils.PrefrenceData

class Splash : AppCompatActivity() {
    private val TIME_OUT = 900

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        executeHandler()

    }

    private fun executeHandler() {
        // Splash screen timer
        Handler().postDelayed({
            // navigate to activity
            if (!PrefrenceData.isUserLoggedIn(this)) {
                startActivity(Intent(this@Splash, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@Splash, DashBoardActivity::class.java))
                finish()
            }

        }, TIME_OUT.toLong())

    }

}
