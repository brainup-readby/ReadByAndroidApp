package com.brainup.readbyapp.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.MainActivity
import com.brainup.readbyapp.MainActivity.Companion.subject_id
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSelectedSubject
import com.brainup.readbyapp.auth.login.UserSubscription
import com.brainup.readbyapp.chapter.ChapterListActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RateAppRequest
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.adapter.SubjectListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.library.CategoryParentAdapter
import com.brainup.readbyapp.library.LibTopicListActivity
import com.brainup.readbyapp.payment.model.Data
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mj.elearning24.base.BaseFragment
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_dash_board_acitivty.*
import java.util.*
import kotlin.collections.HashMap

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: HomeViewModel
    private var userData: UserData? = null
    private lateinit var dialogBuilder: androidx.appcompat.app.AlertDialog
    private lateinit var feedbacktext: EditText
    private lateinit var learningContentRating: RatingBar
    private lateinit var appUsabilityRating: RatingBar
    private lateinit var radioGroupRate: RadioGroup
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button
    private var learningRating: Float = 0.0f
    private var appUsage: Float = 0.0f
    private lateinit var appRadioRating: String
    private var myObject: UserSubscription? = null
    private var courseId: Int = 0
    private var subscriptionId: Int = 0
    private var coursePrice: Double = 0.0
    private var subsFlag: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = MaterialAlertDialogBuilder(this.requireActivity()).create()
        if (arguments != null) {
            myObject = arguments?.getSerializable(ARG_PARAM) as UserSubscription
        }
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
        viewModel = ViewModelProvider(this.requireActivity()).get(HomeViewModel::class.java)
        initProgressbar(this.requireContext())
        if (myObject != null && myObject!!.SUBSCRIPTION_ID > 0) {
            getDataFromSubscrption(myObject!!)
        } else {
            getUserDetails()
        }

        handleToggle()
    }

    companion object {
        private val ARG_PARAM = "subsValue"

        fun newInstance(myObject: UserSubscription): Home {
            val fragment = Home()
            val args = Bundle()
            args.putSerializable(ARG_PARAM, myObject)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getDataFromSubscrption(subs: UserSubscription) {
        if (subs != null) {
            val name = PrefrenceData.getUserName(this.requireContext()).substringBefore(" ")
            getActivity()?.tvHeaderTitle?.text = name
            getActivity()?.tvClass?.text = subs?.MAS_COURSE?.COURSE_NAME
            courseId = subs.MAS_COURSE.COURSE_ID
            subscriptionId = subs.SUBSCRIPTION_ID
            coursePrice = subs.MAS_COURSE.COURSE_PRICE
            subsFlag = subs.SUBSCRIPTION_FLAG
            MainActivity.course_price = subs.MAS_COURSE.COURSE_PRICE
            MainActivity.course_id = subs.MAS_COURSE.COURSE_ID
            MainActivity.subs_id = subs.SUBSCRIPTION_ID
            MainActivity.subscription_flag = subs.SUBSCRIPTION_FLAG
            MainActivity.is_From_Profile = "Y"
            //setPrefsValues(userData)
            tvStudentName.text = "Hi" + " " + name
            showGreeting()
            rvSubjects.adapter =
                    SubjectListAdapter(
                            subs?.MAS_STREAM?.MAS_SUBJECT,
                            SubjectClickHandler(this.requireContext())
                    )
            val subjectListAdapter = CategoryParentAdapter()
            subjectListAdapter.submitList(subs?.MAS_STREAM?.MAS_SUBJECT)
            rvLib.adapter = subjectListAdapter


            shareApps.setOnClickListener {

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val shareSubText = "READBY - The Great Learning app"
                val shareBodyText = "http://play.google.com/store/apps/details?id=com.brainup.readbyapp"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText)
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText)
                startActivity(Intent.createChooser(shareIntent, "Share With"))
            }

            rate_app.setOnClickListener(View.OnClickListener {
                showDialog()
            })
        }else{
            Toast.makeText(requireContext(), "Please try again later.", Toast.LENGTH_SHORT).show()
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
        if (userData != null) {
            val name = userData?.FIRST_NAME
            getActivity()?.tvHeaderTitle?.text = name
            getActivity()?.tvClass?.text = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_NAME
            courseId = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_ID!!
            subscriptionId = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_ID!!
            coursePrice = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_PRICE!!
            if(userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_FLAG != null){
                subsFlag = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_FLAG!!
                MainActivity.subscription_flag = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_FLAG!!
            }else{
                subsFlag = "u"
                MainActivity.subscription_flag = "u"
            }

            //subsFlag = "u"
            MainActivity.course_price = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_PRICE!!
            MainActivity.course_id = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_ID!!
            MainActivity.subs_id = userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_ID!!

            MainActivity.is_From_Profile = "N"
            //MainActivity.subscription_flag = subsFlag
            setPrefsValues(userData)
            tvStudentName.text = "Hi" + " " + name
            showGreeting()
            rvSubjects.adapter =
                    SubjectListAdapter(
                            userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_STREAM?.MAS_SUBJECT,
                            SubjectClickHandler(this.requireContext())
                    )
            val subjectListAdapter = CategoryParentAdapter()
            subjectListAdapter.submitList(userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_STREAM?.MAS_SUBJECT)
            rvLib.adapter = subjectListAdapter


            shareApps.setOnClickListener {

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val shareSubText = "READBY - The Great Learning app"
                val shareBodyText = "http://play.google.com/store/apps/details?id=com.brainup.readbyapp"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText)
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText)
                startActivity(Intent.createChooser(shareIntent, "Share With"))
            }

            rate_app.setOnClickListener(View.OnClickListener {
                showDialog()
            })
        } else {
            Toast.makeText(requireContext(), "Please try again later.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showGreeting() {
        val date = Date()
        val cal: Calendar = Calendar.getInstance()
        cal.setTime(date)
        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)

        var greeting: String
        if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening";
        } else if (hour >= 21 && hour < 24) {
            greeting = "Good Night";
        } else {
            greeting = "Good Morning";
        }

        tvGreetingMsg.text = greeting
        tvGreetingMsg.visibility = View.VISIBLE

    }

    private fun setPrefsValues(userData: UserData?) {
        val fname = userData?.FIRST_NAME
        val lname = userData?.LAST_NAME
        val email = userData?.EMAIL_ID
        val mobile = userData?.MOBILE_NO
        if (fname != null && lname != null) {
            PrefrenceData.setUserName(
                    this.requireContext(),
                    fname.toString() + " " + lname.toString()
            )
        } else {
            PrefrenceData.setUserName(this.requireContext(), fname.toString())
        }
        PrefrenceData.setEmailID(this.requireContext(), email.toString())
        PrefrenceData.setMobNo(this.requireContext(), mobile.toString())
    }

    @SuppressLint("NewApi")
    private fun handleToggle() {
        btnToggle.setOnClickListener {
            if (btnToggle.text == getString(R.string.library)) {
                personalizedHandler()
                btnToggle.text = getString(R.string.personalised)
                val img: Drawable = this.requireContext().getDrawable(R.drawable.ic_location)!!
                btnToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
            } else {
                btnToggle.text = getString(R.string.library)
                libHandler()
                val img: Drawable = this.requireContext().getDrawable(R.drawable.ic_playlist)!!
                btnToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
            }
        }
    }

    private fun personalizedHandler() {
        rvLib.visibility = View.GONE
        rvSubjects.visibility = View.VISIBLE
    }

    private fun libHandler() {
        rvLib.visibility = View.VISIBLE
        rvSubjects.visibility = View.GONE
    }

    inner class SubjectClickHandler(val context: Context) {
        fun onItemClick(userSelectedSubject: UserSelectedSubject) {
            subject_id = userSelectedSubject.SUBJECT_ID.toString()
            val intent = Intent(context, ChapterListActivity::class.java)
            intent.putExtra(ChapterListActivity.KEY_TITLE, userSelectedSubject.SUBJECT_NAME)
            intent.putExtra(ChapterListActivity.KEY_SELECTED_SUBJECT, userSelectedSubject)
            context.startActivity(intent)
        }
    }

    class ChapterClickHandler(val context: Context) {
        fun onItemClick(userSelectedChapters: UserSelectedChapters) {

            val intent = Intent(context, LibTopicListActivity::class.java)
            intent.putExtra(LibTopicListActivity.KEY_TITLE, userSelectedChapters.CHAPTER_NAME)
            intent.putExtra(LibTopicListActivity.KEY_SELECTED_TOPIC, userSelectedChapters)
            context.startActivity(intent)
        }
    }

    private fun showDialog() {
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.rate_app_dialog, null)
        feedbacktext =
                dialogView.findViewById<View>(R.id.comment_text) as EditText
        learningContentRating = dialogView.findViewById<View>(R.id.ratingBar1) as RatingBar
        appUsabilityRating = dialogView.findViewById<View>(R.id.ratingBar2) as RatingBar
        radioGroupRate = dialogView.findViewById<View>(R.id.radio_group) as RadioGroup
        learningContentRating.setOnRatingBarChangeListener(object :
                RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                learningRating = p1
            }
        })

        appUsabilityRating.setOnRatingBarChangeListener(object :
                RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                appUsage = p1

            }
        })

        radioGroupRate.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = dialogView.findViewById<View>(checkedId) as RadioButton
                    appRadioRating = radio.text.toString()

                })

        btnSubmit =
                dialogView.findViewById<View>(R.id.txtPositiveButton) as Button
        btnCancel =
                dialogView.findViewById<View>(R.id.txtNegativeButton) as Button

        btnCancel.setOnClickListener {
            dialogBuilder.dismiss()
        }
        btnSubmit.setOnClickListener {

            progressDialog.show()
            val mobileNumber = PrefrenceData.getMobNo(requireContext()).toLong()
            val userId = PrefrenceData.getUserId(requireContext()).toInt()
            if (learningRating.toInt() > 0 && appUsage.toInt() > 0 && appRadioRating != null) { //feedbacktext.text.toString() != null && !feedbacktext.text.toString().equals("") &&
                val rateAppRequest =
                        RateAppRequest(
                                COMMENTS = feedbacktext.text.toString(),
                                LEARNING = learningRating.toInt(),
                                USABILITY = appUsage.toInt(),
                                CONTENT = appRadioRating,
                                MOBILE_NO = mobileNumber,
                                USER_ID = userId
                        )
                viewModel.rateApp(rateAppRequest)?.observe(requireActivity(), Observer {

                    progressDialog.dismiss()
                    dialogBuilder.dismiss()
                    if (it.isSuccessful) {

                        val body = it.body
                        if (body != null && body.status == Constants.STATUS_SUCCESS) {
                            val data = body.data
                            Toast.makeText(requireContext(), "Thanks for your feedback.", Toast.LENGTH_SHORT).show()
                        }

                    } else if (it.code == Constants.ERROR_CODE) {
                        val msg = it.errorMessage
                        if (!msg.isNullOrBlank()) {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(requireContext(), "Network Error please try again in sometime.", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Please fill all fields for rating.",
                        Toast.LENGTH_SHORT).show()
            }

        }

        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }


}