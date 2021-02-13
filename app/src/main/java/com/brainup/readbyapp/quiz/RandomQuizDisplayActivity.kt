package com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RandomQuizData
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RandomQuizResponse
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.SaveQuizResultRequest
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.RandomQuizResultActivity
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.model.model.RandomQuizModel
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizViewModel
import com.brainup.readbyapp.utils.PrefrenceData
import kotlinx.android.synthetic.main.initializ_qus_activty.*

import kotlinx.android.synthetic.main.quiz_activity_list.*
import kotlinx.android.synthetic.main.quiz_activity_list.tvSubmitQuiz


class RandomQuizDisplayActivity : BaseActivity() {
    var layoutManager: LinearLayoutManager? = null
    var values: Int = 1
    var countMaxValues: Int = 1
    var topic_id: Int = 0
    lateinit var name: String
    lateinit var totalQusNo: String
    private lateinit var viewModel: QuizViewModel

    companion object {
        const val SUBJECT_ID = "subjectId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_activity_list)
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        initProgressbar(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val subjectId = intent.getStringExtra(InitializQuizActivity.SUBJECT_ID)!!
        val title = intent.getStringExtra(InitializQuizActivity.TITLE)
        if (title != null && title.isNotEmpty()) {
            this.title = title
        }
        quizList.addOnItemTouchListener(object : SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return rv.scrollState == RecyclerView.SCROLL_STATE_DRAGGING
            }
        })
        findViewById<Button>(R.id.tvNext).setOnClickListener {
            if (values <= countMaxValues) {
                values = values.plus(1)
                tvNoOfQuesions.setText("Question(" + values + totalQusNo)
            }
            if (layoutManager!!.findLastCompletelyVisibleItemPosition() < ((quizList.adapter as RandomQuizDisplayAdapter).getItemCount() - 1)) {
                layoutManager!!.scrollToPosition(layoutManager!!.findLastCompletelyVisibleItemPosition() + 1)
            }
        }

        findViewById<Button>(R.id.tvPrev).setOnClickListener {
            if (values >= 2) {
                values = values.minus(1)
                tvNoOfQuesions.setText("Question(" + values + totalQusNo)
            }
            if (layoutManager!!.findFirstCompletelyVisibleItemPosition() < ((quizList.adapter as RandomQuizDisplayAdapter).getItemCount() + 1)) {
                layoutManager!!.scrollToPosition(layoutManager!!.findLastCompletelyVisibleItemPosition() - 1)
            }
        }

        findViewById<TextView>(R.id.tvSubmitQuiz).setOnClickListener {
            var list = (quizList.adapter as RandomQuizDisplayAdapter).getAllListWithSelected()
            var lists = RandomQuizModel()
            for (item in list!!) {
                if (item.SET_SELECTION_1) {
                    var model = RandomQuizResponse(
                            item.QUIZ_ID, item.SUBJECT_ID, item.QUSTN_CODE, item.QUSTN_DSC, item.OPTN_1,
                            item.OPTN_2, item.OPTN_3, item.OPTN_4, item.CRRCT_OPTN, true, false, false, false, false
                    )
                    lists.add(model)
                } else if (item.SET_SELECTION_2) {
                    var model = RandomQuizResponse(
                            item.QUIZ_ID, item.SUBJECT_ID, item.QUSTN_CODE, item.QUSTN_DSC, item.OPTN_1,
                            item.OPTN_2, item.OPTN_3, item.OPTN_4, item.CRRCT_OPTN, false, true, false, false, false
                    )
                    lists.add(model)
                } else if (item.SET_SELECTION_3) {
                    var model = RandomQuizResponse(
                            item.QUIZ_ID, item.SUBJECT_ID, item.QUSTN_CODE, item.QUSTN_DSC, item.OPTN_1,
                            item.OPTN_2, item.OPTN_3, item.OPTN_4, item.CRRCT_OPTN, false, false, true, false, false
                    )
                    lists.add(model)
                } else if (item.SET_SELECTION_4) {
                    var model = RandomQuizResponse(
                            item.QUIZ_ID, item.SUBJECT_ID, item.QUSTN_CODE, item.QUSTN_DSC, item.OPTN_1,
                            item.OPTN_2, item.OPTN_3, item.OPTN_4, item.CRRCT_OPTN, false, false, false, true, false
                    )
                    lists.add(model)
                } else if (!item.SET_SELECTION_1 && !item.SET_SELECTION_2 && !item.SET_SELECTION_3 && !item.SET_SELECTION_4) {
                    var model = RandomQuizResponse(
                            item.QUIZ_ID, item.SUBJECT_ID, item.QUSTN_CODE, item.QUSTN_DSC, item.OPTN_1,
                            item.OPTN_2, item.OPTN_3, item.OPTN_4, item.CRRCT_OPTN, false, false, false, false, true
                    )
                    lists.add(model)
                }
            }
            submitDataForTest(lists)
        }
        setRandomQuizData(subjectId)
    }

    private fun submitDataForTest(lists: RandomQuizModel) {

        var countCorrectAnswer = 0
        var attemptedQuestions = 0
        for (item in lists) {
            if (!item.NOT_ATTEMPTED) {
                attemptedQuestions++
            }
            if (item.SET_SELECTION_1) {
                if (item.OPTN_1.contentEquals(item.CRRCT_OPTN)) {
                    countCorrectAnswer++
                }
            } else if (item.SET_SELECTION_2) {
                if (item.OPTN_2.contentEquals(item.CRRCT_OPTN)) {
                    countCorrectAnswer++
                }
            } else if (item.SET_SELECTION_3) {
                if (item.OPTN_3.contentEquals(item.CRRCT_OPTN)) {
                    countCorrectAnswer++
                }
            } else if (item.SET_SELECTION_4) {
                if (item.OPTN_4.contentEquals(item.CRRCT_OPTN)) {
                    countCorrectAnswer++
                }
            }
        }
        var quizModeldata = SaveQuizResultRequest(
                USER_ID = PrefrenceData.getUserId(this).toInt(),
                MOBILE_NO = PrefrenceData.getMobNo(this).toLong(),
                TOTAL_NO_QSTN = lists.size,
                TOTAL_NO_QSTN_ATMPT = attemptedQuestions,
                TOTAL_NO_CORRCT_ANS = countCorrectAnswer
        )
        progressDialog.show()
        viewModel.submitRandomQuizResultData(quizModeldata)?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                if (body?.data?.QUIZ_RESULT_ID!! > 0) {
                    val intent = Intent(this, RandomQuizResultActivity::class.java)
                    intent.putExtra(RandomQuizResultActivity.KEY_TITLE, "Quiz Result")
                    intent.putExtra(RandomQuizResultActivity.KEY_RESULT_LIST, lists)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error while saving quiz results.", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    private fun setRandomQuizData(subjectId: String) {

        progressDialog.show()
        viewModel.getRandomQuestionsData(subjectId)
                ?.observe(this, Observer {
                    progressDialog.dismiss()
                    if (it.isSuccessful) {
                        val body = it.body
                        setUpRandomQuizView(body)
                    }
                })
    }

    private fun setUpRandomQuizView(body: RandomQuizData?) {
        if (body != null) {
            var list = body.data
            if (list != null) {
                noDataFound.visibility = View.GONE
                quizList.visibility = View.VISIBLE
                countMaxValues = list.size - 1
                setAdapterView(list)
            } else {
                tvSubmitQuiz.isEnabled = false
                tvPrev.isEnabled = false
                tvNext.isEnabled = false
                tvNoOfQuesions.setText("Question(" + "0" + "/0)")
                noDataFound.visibility = View.VISIBLE
                quizList.visibility = View.GONE
            }
        } else {
            tvSubmitQuiz.isEnabled = false
            tvPrev.isEnabled = false
            tvNext.isEnabled = false
            tvNoOfQuesions.setText("Question(" + "0" + "/10)")
            noDataFound.visibility = View.VISIBLE
            quizList.visibility = View.GONE
        }
    }

    private fun setAdapterView(list: List<RandomQuizResponse>?) {
        totalQusNo = "/" + list?.size + ")"
        tvNoOfQuesions.setText("Question(" + values + totalQusNo)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        quizList.setLayoutManager(layoutManager)
        quizList.adapter = RandomQuizDisplayAdapter(list, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun setPositionOfQus(position: Int) {}
}