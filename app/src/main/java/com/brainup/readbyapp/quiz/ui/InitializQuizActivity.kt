package com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SimpleOnItemTouchListener
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizResultViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizViewModel
import com.brainup.readbyapp.payment.model.Data
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.quiz.model.model.*
import com.brainup.readbyapp.utils.PrefrenceData
import kotlinx.android.synthetic.main.initializ_qus_activty.*
import kotlinx.android.synthetic.main.initializ_qus_activty.toolbar
import kotlinx.android.synthetic.main.new_activity_player.*

import kotlinx.android.synthetic.main.quiz_activity_list.*


class InitializQuizActivity : BaseActivity() {
    var layoutManager : LinearLayoutManager? = null
    var values : Int =1
    var  countMaxValues: Int = 1
    var topic_id :Int = 0
    lateinit var name: String
    lateinit var  totalQusNo :  String
    private lateinit var viewModel: QuizViewModel
    companion object {
        const val KEY_SELECTED_TOPIC = "keySelectedTopic"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initializ_qus_activty)
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        initProgressbar(this)
        val selectedSubject = intent.getSerializableExtra(KEY_SELECTED_TOPIC) as UserSelectedTopics
        getQusList(selectedSubject.TOPIC_ID.toString())
        //getQusList("1")
        setupToolbar()
        findViewById<TextView>(R.id.tvSubmitQuiz).setOnClickListener {
            val intent = Intent(this@InitializQuizActivity, QuizActivityNew::class.java)
            intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC,selectedSubject)
            startActivity(intent)
        }



    }
    private fun setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun getQusList(id: String) {
        progressDialog.show()
        viewModel.getQuestionList(id)
            ?.observe(QuizActivityNew@ this, Observer {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    val body = it.body
                    setuiView(body)

                }
            })

    }

    private fun setuiView(body: QusListResponse?) {
        qname.setText(body!!.data.QNAME)
        qdes.setText(body!!.data.DETAIL_DESC)
        totalqsn.setText(""+body!!.data.QCOUNT)

    }


}