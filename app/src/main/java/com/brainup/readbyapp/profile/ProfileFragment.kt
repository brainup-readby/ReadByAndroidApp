package com.brainup.readbyapp.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.ChooseAcademicActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LoginActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.profile.ProfileViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.profile.SUbscriptionListActivity
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mj.elearning24.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private lateinit var profileViewModel: ProfileViewModel
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel = ViewModelProvider(this.requireActivity()).get(ProfileViewModel::class.java)
        initProgressbar(this.requireContext())
        setVeiw()
        btnSubscription.setOnClickListener {
            val intent = Intent(this.requireContext(), SUbscriptionListActivity::class.java)
            startActivity(intent)
        }
        signOut.setOnClickListener {
            mLogout()
        }
    }


    private fun mLogout() {
        val items = arrayOf("No", "Yes")
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(
                this.requireContext(),
                R.style.ThemeOverlay_App_MaterialAlertDialog
            ).create()
        MaterialAlertDialogBuilder(this.requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle("Logout")
            .setMessage(getString(R.string.sure_for_logout))
            .setPositiveButton(
                resources.getString(R.string.yes)
            ) { dialog, id ->
                profileViewModel.logout(PrefrenceData.getMobNo(requireContext()))?.observe(requireActivity(), Observer {

                    if (it.isSuccessful) {

                        val body = it.body
                        if (body != null && body.status == Constants.STATUS_SUCCESS) {
                            val data = body.data
                            val mobNo = PrefrenceData.getMobNo(requireContext()).toLong()
                            if(data.MOBILE_NO.equals(mobNo) && data.LOGIN_FLAG.equals("f")){
                                PrefrenceData.setUserLoginFromLogout(this.requireContext(), false)
                                PrefrenceData.setMobNo(this.requireContext(), "")
                                PrefrenceData.setUserId(this.requireContext(),"")
                                PrefrenceData.clearAllData(this.requireContext())
                                val intent = Intent(this.requireContext(), LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }

                        }
                    } else if (it.code == Constants.ERROR_CODE) {
                       // progressDialog.dismiss()
                        val msg = it.errorMessage
                        if (!msg.isNullOrBlank()) {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        }else{

                            Toast.makeText(requireContext(), "Network Error please try again in sometime.", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }.setNegativeButton(
                resources.getString(R.string.no)
            ) { dialog, id ->
            }.setCancelable(false)
            .show()
    }

    private fun setVeiw() {
        tvUserName.setText(PrefrenceData.getUserName(this.requireContext()))
        tvName.setText(PrefrenceData.getUserName(this.requireContext()))
        tvMobileNo.setText(PrefrenceData.getMobNo(this.requireContext()))
        tvEmail.setText(PrefrenceData.getEmailID(this.requireContext()))
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}