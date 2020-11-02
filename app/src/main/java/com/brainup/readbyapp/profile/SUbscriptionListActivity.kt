package com.brainup.readbyapp.com.brainup.readbyapp.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.ChooseAcademicActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.utils.PrefrenceData
import kotlinx.android.synthetic.main.activity_subject_list.*
import kotlinx.android.synthetic.main.activity_subject_list.tvStudentName
import kotlinx.android.synthetic.main.activity_topic_list.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.initializ_qus_activty.*
import kotlinx.android.synthetic.main.initializ_qus_activty.toolbar
import kotlinx.android.synthetic.main.sub_activity_subject_list.*
import kotlinx.android.synthetic.main.sub_activity_subject_list.btnSubscription


class SUbscriptionListActivity :BaseActivity() {
    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_SELECTED_SUBJECT = "keySelectedSubject"
    }

    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titleString = intent.getStringExtra(KEY_TITLE)
        title = titleString
        setContentView(R.layout.sub_activity_subject_list)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        tvStudentName.text = "Hi "+PrefrenceData.getUserName(this)
        setupToolbar()
        initProgressbar(this)
        getUserDetails()


        btnSubscription.setOnClickListener {
            val intent = Intent(this, ChooseAcademicActivity::class.java)
             startActivity(intent)
        }

    }

    private fun getUserDetails() {
        progressDialog.show()
        viewModel.getUserDetails(PrefrenceData.getUserId(this))
            ?.observe(SUbscriptionListActivity@ this, Observer {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    val body = it.body
                    if(body?.data?.USER_SUBSCRIPTION !=null  && body?.data?.USER_SUBSCRIPTION.size>0){
                        var userData = body?.data?.USER_SUBSCRIPTION
                        rvSubscriptionList.adapter = SubscriptionListAdapter(userData, SubsClickHandler(this))
                    }

                    //Toast.makeText(this, body?.data?.USER_ID.toString(), Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    inner class SubsClickHandler(val context: Context) {
        fun onItemClick(userSelectedChapters: UserSelectedChapters) {

            //Toast.makeText(context, vendorDealModel.title + "clicked", Toast.LENGTH_SHORT).show()
        }
    }


}