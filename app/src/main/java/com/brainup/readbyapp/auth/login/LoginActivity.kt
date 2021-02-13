package com.brainup.readbyapp.com.brainup.readbyapp.auth.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.MainActivity
import com.brainup.readbyapp.R
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.otp.VerifyOtpActivity
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tiMobile: EditText
    private lateinit var tiRegisterMobile: EditText
    private lateinit var dialogBuilder: androidx.appcompat.app.AlertDialog

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initProgressbar(this)
        dialogBuilder = MaterialAlertDialogBuilder(this).create()
        tiMobile = etMobile
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        btnLogin.setOnClickListener {
            loginViewModel.loginDataChanged(etMobile.text.toString(), true)
            //showBottomSheet()
            //   showDialog()

        }
        btnReg.setOnClickListener {
            // startActivity(Intent(this, ChooseCourseActivity::class.java))
            /*  val intent = Intent(this, RegistrationActivity::class.java)
              startActivity(intent)*/
            showDialog()
        }
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer
            if (loginState.mobNoError != null && loginState.isLoginData) {
                tiMobile.error = getString(loginState.mobNoError)
            }
            if (loginState.mobNoError != null && !loginState.isLoginData) {
                tiRegisterMobile.error = getString(loginState.mobNoError)
            }
            if (loginState.isDataValid && loginState.isLoginData) {

                callApiToCheckMultipleLogin()


            } else if (loginState.isDataValid && !loginState.isLoginData) {
                callSendRegOtpApi()
            }
            /* loginContainer.setOnTouchListener { v: View?, _: MotionEvent? ->
                 if (v !is EditText) {
                     Utils.hideKeypad(this)
                 }
                 false
             }*/
        })

        //showBottomSheet()callSendLoginOtpApi()
    }

    private fun callApiToCheckMultipleLogin() {
        progressDialog.show()
        var token = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
        loginViewModel.checkMultipleLogin1(etMobile.text.toString(), token)?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                if (body != null && body.status == Constants.STATUS_SUCCESS) {
                    val data = body.data
                    if(data.TOKEN.contentEquals(token)){
                        callSendLoginOtpApi()
                    }else{
                        val items = arrayOf("Logout")
                        val alertDialogBuilder =
                            MaterialAlertDialogBuilder(
                                this,
                                R.style.ThemeOverlay_App_MaterialAlertDialog
                            ).create()
                        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                            .setTitle("Alert!")
                            .setMessage(getString(R.string.multiple_login))
                            .setPositiveButton(
                                resources.getString(R.string.ok)
                            ) { dialog, id ->
                                //mLogout(etMobile.text.toString())
                                alertDialogBuilder.dismiss()
                            }.setCancelable(false)
                            .show()
                    }

                }
            } else if (it.code == Constants.ERROR_CODE) {
                //progressDialog.dismiss()
                if (!it.errorMessage.isNullOrEmpty()) {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Network Error. please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun mLogout(mobileNumber:String) {
        loginViewModel.logout(mobileNumber)?.observe(this, Observer {
            if (it.isSuccessful) {
                val body = it.body
                if (body != null && body.status == Constants.STATUS_SUCCESS) {
                    val data = body.data
                    if(data.MOBILE_NO.toString().contentEquals(mobileNumber) && data.LOGIN_FLAG.contentEquals("f")){
                        PrefrenceData.setUserLoginFromLogout(this, false)
                        PrefrenceData.setMobNo(this, "")
                        PrefrenceData.setUserId(this,"")
                        PrefrenceData.clearAllData(this)
                       // dialogBuilder.dismiss()
//                        val intent = Intent(this, LoginActivity::class.java)
//                        intent.flags =
//                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
                    }

                }
            } else if (it.code == Constants.ERROR_CODE) {
                // progressDialog.dismiss()
                val msg = it.errorMessage
                if (!msg.isNullOrBlank()) {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }else{

                    Toast.makeText(this, "Network Error please try again in sometime.", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callSendLoginOtpApi() {
        progressDialog.show()
        loginViewModel.login(etMobile.text.toString())?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {

             //   var    otp    =  it.body?.data
                val intent = Intent(this, VerifyOtpActivity::class.java)
                intent.putExtra(Constants.KEY_MOB_NO, etMobile.text.toString())
                //intent.putExtra(Constants.KEY_OTP, otp)
                startActivity(intent)
                finish()
            } else if (it.code == Constants.ERROR_CODE) {
                if (!it.errorMessage.isNullOrEmpty()) {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun callSendRegOtpApi() {
        progressDialog.show()
        loginViewModel.sendRegOtp(tiRegisterMobile.text.toString())?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val intent = Intent(this, VerifyOtpActivity::class.java)
                intent.putExtra(Constants.KEY_MOB_NO, tiRegisterMobile.text.toString())
                intent.putExtra(VerifyOtpActivity.KEY_REG_SCREEN_TYPE, true)
                startActivity(intent)
                dialogBuilder.dismiss()
                finish()
            } else if (it.code == Constants.ERROR_CODE) {
                if (!it.errorMessage.isNullOrEmpty()) {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    /*   private fun showBottomSheet() {
           val dialog = BottomSheetDialog(this)
           val bottomSheet = layoutInflater.inflate(R.layout.bootm_sheet_validation, null)
           *//*bottomSheet.btnResetSubmit.setOnClickListener {
            // dialog.dismiss()
        }*//*
        // dialog.setCancelable(false)
        dialog.setContentView(bottomSheet)
        //dialog.setCancelable(false)
        //dialog.show()
    }*/

    private fun showDialog() {
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.mob_no_dialog, null)
        tiRegisterMobile =
            dialogView.findViewById<View>(R.id.editText) as EditText
        val btnSubmit: Button =
            dialogView.findViewById<View>(R.id.txtPositiveButton) as Button
        val btnCancel: Button =
            dialogView.findViewById<View>(R.id.txtNegativeButton) as Button
        tiRegisterMobile.setText("")

        btnCancel.setOnClickListener {
            dialogBuilder.dismiss()
        }
        btnSubmit.setOnClickListener { // DO SOMETHINGS
            loginViewModel.loginDataChanged(tiRegisterMobile.text.toString(), false)
        }

        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }
}