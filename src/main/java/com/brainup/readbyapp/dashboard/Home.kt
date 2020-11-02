package com.brainup.readbyapp.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.auth.login.UserSelectedSubject
import com.brainup.readbyapp.chapter.ChapterListActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.adapter.SubjectListAdapter
import com.brainup.readbyapp.utils.PrefrenceData
import com.mj.elearning24.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_dash_board_acitivty.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: HomeViewModel
    private var userData: UserData? = null

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        initProgressbar(this.requireContext())
        getUserDetails()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getUserDetails() {
        progressDialog.show()
        viewModel.getUserDetails(PrefrenceData.getUserId(this.requireContext()))
            ?.observe(Home@ this, Observer {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    val body = it.body
                    userData = body?.data
                    setData()
                    //Toast.makeText(this, body?.data?.USER_ID.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setData() {
        val name = userData?.FIRST_NAME
        getActivity()?.tvHeaderTitle?.text = name
        PrefrenceData.setUserName(this.requireContext(), name.toString())
        var  namev  =  "Hi"+  " "+name
        tvStudentName.text = namev
        rvSubjects.adapter =
            SubjectListAdapter(
                userData?.USER_SUBSCRIPTION?.get(0)?.MAS_STREAM?.MAS_SUBJECT,
                SubjectClickHandler(this.requireContext())
            )
    }

    inner class SubjectClickHandler(val context: Context) {
        fun onItemClick(userSelectedSubject: UserSelectedSubject) {
            val intent = Intent(context, ChapterListActivity::class.java)
            intent.putExtra(ChapterListActivity.KEY_TITLE, userSelectedSubject.SUBJECT_NAME)
            intent.putExtra(ChapterListActivity.KEY_SELECTED_SUBJECT, userSelectedSubject)
            context.startActivity(intent)
            //Toast.makeText(context, vendorDealModel.title + "clicked", Toast.LENGTH_SHORT).show()
        }
    }
}