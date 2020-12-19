package com.brainup.readbyapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LoginActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LoginViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.DashBoardActivity
import com.brainup.readbyapp.profile.SplashViewModel
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*

class Splash : AppCompatActivity() {
    private val TIME_OUT = 900
    //private lateinit var viewModel: SplashViewModel
    //private lateinit var dialogBuilder: androidx.appcompat.app.AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
      //  dialogBuilder = MaterialAlertDialogBuilder(this).create()
      //  viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
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
               // callApiToCheckMultipleLogins()
                startActivity(Intent(this@Splash, DashBoardActivity::class.java))
                finish()

            }

        }, TIME_OUT.toLong())

    }

//    private fun mLogout() {
//
//        viewModel.logout(PrefrenceData.getMobNo(this))?.observe(this, Observer {
//
//            if (it.isSuccessful) {
//
//                val body = it.body
//                if (body != null && body.status == Constants.STATUS_SUCCESS) {
//                    val data = body.data
//                    if(data.MOBILE_NO.toString().equals(PrefrenceData.getMobNo(this)) && data.LOGIN_FLAG.equals("f")){
//                        PrefrenceData.setUserLoginFromLogout(this, false)
//                        PrefrenceData.setMobNo(this, "")
//                        PrefrenceData.setUserId(this,"")
//                        PrefrenceData.clearAllData(this)
//                        val intent = Intent(this, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//
//                }
//            } else if (it.code == Constants.ERROR_CODE) {
//                // progressDialog.dismiss()
//                val msg = it.errorMessage
//                if (!msg.isNullOrBlank()) {
//                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
//                }else{
//
//                    Toast.makeText(this, "Network Error please try again in sometime.", Toast.LENGTH_LONG).show()
//                }
//            }
//        })
//    }
//
//    private fun callApiToCheckMultipleLogins() {
//    //    progressDialog.show()
//        viewModel.checkMultipleLogin(PrefrenceData.getMobNo(this))?.observe(this, Observer {
//          //  progressDialog.dismiss()
//            if (it.isSuccessful) {
//
//                val body = it.body
//                if (body != null && body.status == Constants.STATUS_SUCCESS) {
//                    val data = body.data
//                    if(!data){
//                        val items = arrayOf("Logout")
//                        val alertDialogBuilder =
//                                MaterialAlertDialogBuilder(
//                                        this,
//                                        R.style.ThemeOverlay_App_MaterialAlertDialog
//                                ).create()
//                        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
//                                .setTitle("Alert!")
//                                .setMessage(getString(R.string.multiple_login))
//                                .setPositiveButton(
//                                        resources.getString(R.string.Logout)
//                                ) { dialog, id ->
//                                    mLogout()
//                                }.setCancelable(false)
//                                .show()
//                    }else{
//                        startActivity(Intent(this@Splash, DashBoardActivity::class.java))
//                        finish()
//                    }
//
//                }
//            } else if (it.code == Constants.ERROR_CODE) {
//                //progressDialog.dismiss()
//                if (!it.errorMessage.isNullOrEmpty()) {
//                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(this, "Network Error. please try again later.", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
//    }

}
