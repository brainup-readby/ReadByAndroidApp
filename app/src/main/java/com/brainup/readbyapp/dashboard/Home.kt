package com.brainup.readbyapp.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.adapter.CustomArrayAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.auth.model.ModelDisplayName
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.adapter.SubjectListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RateAppRequest
import com.brainup.readbyapp.com.brainup.readbyapp.library.CategoryParentAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.InitializQuizActivity
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.QuizActivityNew
import com.brainup.readbyapp.library.LibTopicListActivity
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.mj.elearning24.base.BaseFragment
import kotlinx.android.synthetic.main.activity_choose_academic.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_dash_board_acitivty.*
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : BaseFragment(), OnChartValueSelectedListener {
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
    var countTopics: Int = 0
    var countTopicStatus: Int = 0
    private lateinit var outlineBoard: TextInputLayout
    private var subjectIdForSpinner: Long? = 0
    private var subjectNameForSpinner: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //outlineBoard = subjectListForTest
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
            setSubjectForTest(subs?.MAS_STREAM?.MAS_SUBJECT)
            createProgressChart(subs?.MAS_STREAM?.MAS_SUBJECT)

            shareApps.setOnClickListener {

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val shareSubText = "READBY - The Great Learning app"
                val shareBodyText =
                    "http://play.google.com/store/apps/details?id=com.brainup.readbyapp"
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

    private fun getUserDetails() {
        progressDialog.show()
        viewModel.getUserDetails(PrefrenceData.getUserId(this.requireContext()))
            ?.observe(Home@ this, Observer {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    val body = it.body
                    userData = body?.data
                    setData()
                    // createProgressChart()
                    //Toast.makeText(this, body?.data?.USER_ID.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setData() {
        if (userData != null) {
            val name = userData?.FIRST_NAME
            getActivity()?.tvHeaderTitle?.text = name
            getActivity()?.tvClass?.text =
                userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_NAME
            courseId =
                userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_ID!!
            subscriptionId =
                userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_ID!!
            coursePrice =
                userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_PRICE!!
            if (userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_FLAG != null) {
                subsFlag =
                    userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_FLAG!!
                MainActivity.subscription_flag =
                    userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_FLAG!!
            } else {
                subsFlag = "u"
                MainActivity.subscription_flag = "u"
            }

            //subsFlag = "u"
            MainActivity.course_price =
                userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_PRICE!!
            MainActivity.course_id =
                userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_COURSE?.COURSE_ID!!
            MainActivity.subs_id =
                userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.SUBSCRIPTION_ID!!

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
            setSubjectForTest(userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_STREAM?.MAS_SUBJECT)
            createProgressChart(userData?.USER_SUBSCRIPTION?.get(userData!!.USER_SUBSCRIPTION.size - 1)?.MAS_STREAM?.MAS_SUBJECT)

            shareApps.setOnClickListener {

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val shareSubText = "READBY - The Great Learning app"
                val shareBodyText =
                    "http://play.google.com/store/apps/details?id=com.brainup.readbyapp"
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
                            Toast.makeText(
                                requireContext(),
                                "Thanks for your feedback.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else if (it.code == Constants.ERROR_CODE) {
                        val msg = it.errorMessage
                        if (!msg.isNullOrBlank()) {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Network Error please try again in sometime.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
            } else {
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(), "Please fill all fields for rating.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }


    private fun createProgressChart(masSubject: List<UserSelectedSubject>?) {

        if (masSubject != null) {
            for (subjects in masSubject) {
                for (chapters in subjects.MAS_CHAPTERS) {
                    for (topics in chapters.MAS_TOPICS) {
                        if (topics != null) {
                            countTopics++;
                            if (topics.MAS_TOPIC_STATUS != null) {
                                countTopicStatus++
                            }
                        }
                    }
                }
                var chartPercentage: Float = (((countTopicStatus * 100) / countTopics).toFloat())
                if (chartPercentage.toInt() > 0) {
                    speratorLineProgress.visibility = View.VISIBLE
                    studentProgressText.visibility = View.VISIBLE
                    chart.visibility = View.VISIBLE
                    chart.setUsePercentValues(true)
                    chart.description.isEnabled = false
                    chart.setExtraOffsets(5f, 10f, 5f, 5f)

                    chart.dragDecelerationFrictionCoef = 0.95f

                    // chart.setCenterTextTypeface(tfLight)
                    chart.centerText = generateCenterSpannableText()

                    chart.isDrawHoleEnabled = true
                    chart.setHoleColor(Color.WHITE)

                    chart.setTransparentCircleColor(Color.WHITE)
                    chart.setTransparentCircleAlpha(110)

                    chart.holeRadius = 1f
                    chart.transparentCircleRadius = 1f

                    chart.setDrawCenterText(true)
                    chart.rotationAngle = 0f
                    chart.isRotationEnabled = true
                    chart.isHighlightPerTapEnabled = true
                    chart.setOnChartValueSelectedListener(this)

                    chart.animateY(1400, Easing.EaseInOutQuad)
                    val l = chart.legend
                    l.isEnabled = false
                    chart.setEntryLabelColor(Color.BLACK)
                    chart.setEntryLabelTextSize(12f)
                    setChartData(masSubject)
                }
            }
        }


    }

    private fun setChartData(masSubject: List<UserSelectedSubject>?) {
        val entries = ArrayList<PieEntry>()
        if (masSubject != null) {
            for (subjects in masSubject) {
                for (chapters in subjects.MAS_CHAPTERS) {
                    for (topics in chapters.MAS_TOPICS) {
                        if (topics != null) {
                            countTopics++;
                            if (topics.MAS_TOPIC_STATUS != null) {
                                countTopicStatus++
                            }
                        }
                    }
                }
                var chartPercentage: Float = (((countTopicStatus * 100) / countTopics).toFloat())
                entries.add(PieEntry(chartPercentage, subjects.SUBJECT_NAME))
            }
        }
        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        chart.setData(data)

        // undo all highlights
        chart.highlightValues(null)
        chart.invalidate()
    }

    private fun generateCenterSpannableText(): SpannableString? {
        val s = SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 14, s.length, 0)
        return s
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) {
            Toast.makeText(
                requireContext(), "Student Progress is not available.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", index: " + h!!.x
                    + ", DataSet index: " + h.dataSetIndex
        )
    }

    override fun onNothingSelected() {
        Log.i(
            "VAL SELECTED",""
        )
    }

    private fun bindCustomAdapter(list: ArrayList<ModelDisplayName>): CustomArrayAdapter {
        return CustomArrayAdapter(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
                R.id.tvDropDown,
                list
        )
    }

    private fun setSubjectForTest(masSubject: List<UserSelectedSubject>?) {
        if (!masSubject.isNullOrEmpty()) {
            var subjectList = ArrayList<ModelDisplayName>()
            masSubject?.forEach {

                if(it.SUBJECT_ID > 0){
                    subjectList.add(
                            ModelDisplayName(
                                    title = it.SUBJECT_NAME,
                                    id = it.SUBJECT_ID.toLong()
                            )
                    )
                }else{
                    Toast.makeText(
                            requireContext(), "Error while fetching Subject.",
                            Toast.LENGTH_SHORT
                    ).show()
                }

            }
            subjectSpinnerForTest.setAdapter(bindCustomAdapter(subjectList))
            subjectSpinnerForTest.setOnItemClickListener { parent, view, position, id ->
                subjectIdForSpinner = masSubject?.get(position)?.SUBJECT_ID.toLong()
                subjectNameForSpinner = masSubject?.get(position)?.SUBJECT_NAME
                if(subjectIdForSpinner!!.toInt() > 0){
                    val intent = Intent(requireContext(), InitializQuizActivity::class.java)
                    intent.putExtra(InitializQuizActivity.SUBJECT_NAME, subjectNameForSpinner)
                    intent.putExtra(InitializQuizActivity.SUBJECT_ID, subjectIdForSpinner.toString())
                    intent.putExtra(InitializQuizActivity.RANDOM_QUIZ_FLAG,  "Y")
                    startActivity(intent)
                }
            }
        }else{
            Toast.makeText(
                    requireContext(), "Error while fetching Subject.",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

}