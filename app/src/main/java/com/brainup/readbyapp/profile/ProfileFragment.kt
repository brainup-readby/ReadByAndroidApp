package com.brainup.readbyapp.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.brainup.readbyapp.R
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.ChooseAcademicActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LoginActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.registration.RegistrationActivity
import com.brainup.readbyapp.com.brainup.readbyapp.profile.SUbscriptionListActivity
import com.brainup.readbyapp.utils.PrefrenceData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_profile.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
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

        setVeiw()
        btnSubscription.setOnClickListener {
            val intent = Intent(this.requireContext(), SUbscriptionListActivity::class.java)
             startActivity(intent)


           /* val intent = Intent(this, ChooseAcademicActivity::class.java)
            startActivity(intent)*/

        }

        signOut.setOnClickListener {
            mLogout()
        }
    }


    private fun mLogout() {
            val items = arrayOf("No", "Yes")
            val alertDialogBuilder =
                MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_App_MaterialAlertDialog).create()
               MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Logout")
                .setMessage(getString(R.string.sure_for_logout))
                  .setPositiveButton(
                      resources.getString(R.string.yes)
                  ) { dialog, id ->
                      PrefrenceData.setUserLoginFromLogout(this.requireContext(),false)
                      val intent = Intent(this.requireContext(), LoginActivity::class.java)
                      startActivity(intent)


                  }.setNegativeButton(
                      resources.getString(R.string.no)
                  ) { dialog, id ->



                   }.setCancelable(false)
                .show()



    }





    private fun setVeiw() {
        tvUserName.setText( PrefrenceData.getUserName(this.requireContext()))
        tvName.setText( PrefrenceData.getUserName(this.requireContext()))
        tvMobileNo.setText( PrefrenceData.getMobNo(this.requireContext()))
        tvEmail.setText( PrefrenceData.getEmailID(this.requireContext()))

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