package com.brainup.readbyapp.com.brainup.readbyapp.auth.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.ChooseCourseActivity
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.ChooseCompetitiveActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.ChooseAcademicActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.otp.VerifyOtpActivity
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.toolbar
import kotlinx.android.synthetic.main.common_header.*
import kotlinx.android.synthetic.main.initializ_qus_activty.*

class RegistrationActivity : BaseActivity() {
    private lateinit var viewModel: RegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initProgressbar(this)
        setupToolbar()
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        val mobNo = intent.getStringExtra(Constants.KEY_MOB_NO)
        etMobile.setText(mobNo)
        viewModel.registrationFormState.observe(this, Observer {
            val regState = it ?: return@Observer
            if (regState.firstNameError != null) {
                etFirstName.error = getString(regState.firstNameError)
            }
            if (regState.lastNameError != null) {
                etLastName.error = getString(regState.lastNameError)
            }
            if (regState.mobNoError != null) {
                etMobile.error = getString(regState.mobNoError)
            }
            if (regState.emailError != null) {
                etEmail.error = getString(regState.emailError)
            }
            if (regState.isDataValid) {
                //  Toast.makeText(this, "Api call", Toast.LENGTH_SHORT).show()
                //navigateToOtpVerify()
                callRegistrationApi()
            }
        })

        btnReg.setOnClickListener {
            viewModel.registrationFormState(
                etFirstName.text.toString(),
                etLastName.text.toString(),
                etMobile.text.toString(),
                etEmail.text.toString()
            )

        }

        headerTitle.text = "Registration"
        iv_logo.visibility = View.GONE
        tvGreetingMsg.visibility = View.GONE

    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun navigateToOtpVerify() {
        val intent = Intent(this, VerifyOtpActivity::class.java)
        intent.putExtra(Constants.KEY_MOB_NO, etMobile.text.toString())
        intent.putExtra(VerifyOtpActivity.KEY_REG_SCREEN_TYPE, true)
        startActivity(intent)
    }

    private fun callRegistrationApi() {
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val email = etEmail.text.toString()
        val mobNo = etMobile.text.toString()
        progressDialog.show()
        val userInfo = UserInfo(
            FIRST_NAME = firstName,
            LAST_NAME = lastName,
            MOBILE_NO = mobNo,
            EMAIL_ID = email
        )
        viewModel.registration(userInfo)?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                // Utils.showDialog(this)
                if (body != null && body.status == Constants.STATUS_SUCCESS) {
                    val data = body.data
                    PrefrenceData.setMobNo(this, mobNo)
                    PrefrenceData.setUserId(this, data.USER_ID.toString())
                    val intent = Intent(this, ChooseAcademicActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Toast.makeText(this, "You are successfully registered", Toast.LENGTH_SHORT).show()
            } else if (it.code == Constants.ERROR_CODE) {
                val msg = it.errorMessage
                if (!msg.isNullOrBlank()) {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun navigateToChooseSubject() {
        val intent = Intent(this, ChooseCourseActivity::class.java)
        startActivity(intent)

    }

    private fun showDialog() {
        val items = arrayOf("Competitive Exam", "Academic")

        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle("Choose Course")
            .setItems(items) { dialog, which ->
                // Respond to item chosen
                if (which == 0) {
                    Toast.makeText(this, "Currently not available", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ChooseCompetitiveActivity::class.java)
                    startActivity(intent)
                    dialog.dismiss()

                } else {
                    val intent = Intent(this, ChooseAcademicActivity::class.java)
                    startActivity(intent)
                    dialog.dismiss()
                }
                //Toast.makeText(this@LoginActivity, items[which], Toast.LENGTH_SHORT).show()
            }.setCancelable(false)
            .show()
    }
}