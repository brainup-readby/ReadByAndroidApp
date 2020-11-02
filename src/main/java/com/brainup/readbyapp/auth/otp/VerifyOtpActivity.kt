package com.brainup.readbyapp.com.brainup.readbyapp.auth.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.ChooseCompetitiveActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.ChooseAcademicActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.registration.RegistrationActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.DashBoardActivity
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.chaos.view.PinView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_verify_otp.*
import kotlinx.android.synthetic.main.common_header.*
import java.util.concurrent.TimeUnit


class VerifyOtpActivity : BaseActivity() {
    private var time: Long = 20000
    private lateinit var pinView: PinView
    private var isCallingFromReg: Boolean = false
    private lateinit var mobNo: String
    private lateinit var viewModel: VerifyOtpViewModel

    companion object {
        const val KEY_REG_SCREEN_TYPE = "keyScreenType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
        initProgressbar(this)
        mobNo = if (intent.hasExtra(Constants.KEY_MOB_NO)) {
            intent.getStringExtra(Constants.KEY_MOB_NO)
        } else {
            ""
        }
        viewModel = ViewModelProvider(this).get(VerifyOtpViewModel::class.java)
        // Verify
        // To login to ReadBy
        tvEditMobNo.text = String.format(
            getString(R.string.mob_no_format_with_91),
            intent.getStringExtra(Constants.KEY_MOB_NO)
        )
        headerTitle.text = "Verify"
        if (!intent.getBooleanExtra(VerifyOtpActivity.KEY_REG_SCREEN_TYPE, false)) {
            tvGreetingMsg.text = "To login to ReadBy"
        } else {
            isCallingFromReg = true
            tvGreetingMsg.text = "To register to ReadBy"
        }

        startTimer()
        ivEditNumber.setOnClickListener {
            finish()
        }
        pinView = firstPinView
        pinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.length == 4) {
                    if (isCallingFromReg) {
                        callRegistrationVerifyOtp(s.toString())
                    } else {
                        callLoginVerifyOtp(s.toString())
                    }

                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        btnResend.setOnClickListener {
            callResendApi()
        }
    }

    private fun callResendApi() {
        progressDialog.show()
        viewModel.resendOtp(mobNo)?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
            } else if (it.code == Constants.ERROR_CODE) {
                if (!it.errorMessage.isNullOrEmpty()) {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashBoardActivity::class.java)
        startActivity(intent)
    }


    private fun startTimer() {
        object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //val formater=SimpleDateFormat("ss")
                /*tvTimer.text = String.format(
                    "00:%2d",
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                )*/
                tvTimer.text = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                )

            }

            override fun onFinish() {
                // tvTimer.text = "Time Out!"
                //submitQuiz()
                tvTimer.text = "00:00"
                tvTimer.visibility = View.GONE
                btnResend.visibility = View.VISIBLE
            }
        }.start()
    }


    private fun callLoginVerifyOtp(otp: String) {
        progressDialog.show()
        viewModel.loginOtpVerify(mobNo, otp)?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                if (body != null && body.status == Constants.STATUS_SUCCESS) {
                    val data = body.data
                    if (data != null) {
                        if (data.USER_SUBSCRIPTION.isNullOrEmpty()) {
                            PrefrenceData.setMobNo(this, mobNo)
                            PrefrenceData.setUserId(this, data.USER_ID.toString())
                            //Utils.showDialog(this)
                            val intent = Intent(this, ChooseAcademicActivity::class.java)
                            startActivity(intent)
                        } else {
                            PrefrenceData.setMobNo(this, mobNo)
                            PrefrenceData.setUserId(this, data.USER_ID.toString())
                            PrefrenceData.setUserLogin(this)
                            navigateToDashboard()
                            finish()
                        }

                    } else {
                        pinView.setText("")
                        Toast.makeText(this, "Wrong otp enter", Toast.LENGTH_SHORT).show()
                    }
                    //finish()
                }
            } else if (it.code == Constants.ERROR_CODE) {
                pinView.setText("")
                val msg = it.errorMessage
                if (!msg.isNullOrBlank()) {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callRegistrationVerifyOtp(otp: String) {
        progressDialog.show()
        viewModel.verify(mobNo, otp)?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                if (body != null && body.status == Constants.STATUS_SUCCESS) {
                    val data = body.data
                    if (data) {
                        val intent = Intent(this, RegistrationActivity::class.java)
                        intent.putExtra(Constants.KEY_MOB_NO, mobNo)
                        startActivity(intent)
                        finish()
                    } else {
                        pinView.setText("")
                        Toast.makeText(this, "Wrong otp enter", Toast.LENGTH_SHORT).show()
                    }
                    //finish()
                }
            } else if (it.code == Constants.ERROR_CODE) {
                pinView.setText("")
                val msg = it.errorMessage
                if (!msg.isNullOrBlank()) {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun showDialog() {
        val items = arrayOf("Competitive Exam", "Academic")
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog).create()
        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle("Choose Course")
            .setItems(items) { dialog, which ->
                // Respond to item chosen
                if (which == 0) {
                    Toast.makeText(this, "Currently not available", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ChooseCompetitiveActivity::class.java)
                    startActivity(intent)
                    alertDialogBuilder.dismiss()

                } else {
                    val intent = Intent(this, ChooseAcademicActivity::class.java)
                    startActivity(intent)
                    alertDialogBuilder.dismiss()
                }
                //Toast.makeText(this@LoginActivity, items[which], Toast.LENGTH_SHORT).show()
            }.setCancelable(false)
            .show()
    }
}