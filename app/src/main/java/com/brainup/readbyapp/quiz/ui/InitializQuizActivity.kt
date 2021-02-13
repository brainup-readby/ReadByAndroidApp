package com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RandomQuizData
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizViewModel
import com.brainup.readbyapp.quiz.model.model.*
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
        const val SUBJECT_ID = "subjectId"
        const val SUBJECT_NAME = "subjectName"
        const val RANDOM_QUIZ_FLAG = "randomQuizFlag"
        const val TITLE = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initializ_qus_activty)
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        initProgressbar(this)
        val randomQuizFlag = intent.getStringExtra(RANDOM_QUIZ_FLAG)!!
        if(randomQuizFlag.contentEquals("Y")){
            val subjectId = intent.getStringExtra(SUBJECT_ID)!!
            val subjectName = intent.getStringExtra(SUBJECT_NAME)!!
            setRandomQuizData(subjectId, subjectName, randomQuizFlag)
            findViewById<TextView>(R.id.tvSubmitQuiz).setOnClickListener {
                val intent = Intent(this@InitializQuizActivity, RandomQuizDisplayActivity::class.java)
                intent.putExtra(QuizActivityNew.TITLE, "$subjectName Quiz")
                intent.putExtra(QuizActivityNew.SUBJECT_NAME, subjectName)
                intent.putExtra(QuizActivityNew.SUBJECT_ID, subjectId)
                intent.putExtra(QuizActivityNew.RANDOM_QUIZ_FLAG,  "Y")
                startActivity(intent)
            }
        }
        if(randomQuizFlag!!.contentEquals("N")){
            val selectedSubject = intent.getSerializableExtra(KEY_SELECTED_TOPIC) as UserSelectedTopics
            getQusList(selectedSubject.TOPIC_ID.toString())
            findViewById<TextView>(R.id.tvSubmitQuiz).setOnClickListener {
                val intent = Intent(this@InitializQuizActivity, QuizActivityNew::class.java)
                intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC,selectedSubject)
                startActivity(intent)
            }
        }
        setupToolbar()
    }

    private fun setRandomQuizData(subjectId: String, subjectName: String, randomQuizFlag: String) {

        progressDialog.show()
        viewModel.getRandomQuestionsData(subjectId)
                ?.observe(QuizActivityNew@ this, Observer {
                    progressDialog.dismiss()
                    if (it.isSuccessful) {
                        val body = it.body
                        setUpRandomQuizView(body, subjectName, randomQuizFlag)

                    }
                })
    }

    private fun setUpRandomQuizView(body: RandomQuizData?, subjectName: String, randomQuizFlag: String) {

        if(body != null){

            if(body.data.isNotEmpty()){

                    qname.setText(subjectName)
                    qdes.setText("Random Quiz")
                    totalqsn.setText(""+body.data.size)

            }else{
                Toast.makeText(
                        this@InitializQuizActivity,
                        "Quiz not available.",
                        Toast.LENGTH_LONG
                ).show()
                onBackPressed()
            }
        }else{
            Toast.makeText(
                    this@InitializQuizActivity,
                    "Quiz not available.",
                    Toast.LENGTH_LONG
            ).show()
            onBackPressed()
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

        if(body != null){

            if(body.data != null){

                if(body.data.QNAME != null){
                    qname.setText(body.data.QNAME)
                }else{
                    Toast.makeText(
                            this@InitializQuizActivity,
                            "Quiz not available.",
                            Toast.LENGTH_LONG
                    ).show()
                    onBackPressed()
                }
                if(body.data.DETAIL_DESC != null){
                    qdes.setText(body!!.data!!.DETAIL_DESC!!)
                }
                if(""+body.data.QCOUNT != null){
                    totalqsn.setText(""+body!!.data.QCOUNT!!)
                }
            }else{
                Toast.makeText(
                        this@InitializQuizActivity,
                        "Quiz not available.",
                        Toast.LENGTH_LONG
                ).show()
                onBackPressed()
            }
        }else{
            Toast.makeText(
                    this@InitializQuizActivity,
                    "Quiz not available.",
                    Toast.LENGTH_LONG
            ).show()
            onBackPressed()
        }
    }
}