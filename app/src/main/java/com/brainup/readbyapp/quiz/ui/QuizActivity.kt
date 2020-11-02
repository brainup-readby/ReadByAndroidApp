package com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.utils.DialogUtils
import com.mj.elearning24.quiz.model.QuestionIdOptionSelected
import com.mj.elearning24.quiz.model.Quiz
import com.mj.elearning24.quiz.model.SubmitQuestionRequestModel
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizViewModel
import kotlinx.android.synthetic.main.activity_quiz.*
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


class QuizActivity : BaseActivity() {
    companion object {
        const val KEY_QUESTIONS = "keyQuestions"
        const val KEY_SUBJECT_NAME = "keySubject"
        const val KEY_QUIZ_ID = "keyID"
        const val KEY_DURATION = "keyTime"
        const val KEY_OPTION_1 = "option1"
        const val KEY_OPTION_2 = "option2"
        const val KEY_OPTION_3 = "option3"
        const val KEY_OPTION_4 = "option4"
    }
    private lateinit var viewModel: QuizViewModel
    private lateinit var questionList: ArrayList<Quiz>
    private var selectedQuestionIndex = 0
    private lateinit var quizId: String
    private var quizTime: Long = 900000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        initProgressbar(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        questionList = intent.getSerializableExtra(KEY_QUESTIONS) as ArrayList<Quiz>
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        title = intent.getStringExtra(KEY_SUBJECT_NAME)
        quizId = intent.getStringExtra(KEY_QUIZ_ID)!!
        val duration = intent.getStringExtra(KEY_DURATION)
        if (duration != null && duration.isNotEmpty()) {
            quizTime = TimeUnit.MINUTES.toMillis(duration.toLong())
        }

        // getQuizList(quizId)
        initUi()
        // Toast.makeText(this, "size of list is:->${questionList.size}", Toast.LENGTH_SHORT).show()

        tvNext.setOnClickListener {
            //questionList[selectedQuestionIndex].selectedAns=
            val selectedId: Int = rgAns.checkedRadioButtonId
            if (selectedId != -1) {
                // val radioButton = findViewById<View>(selectedId) as RadioButton
                /* Toast.makeText(
                     this@QuizActivity,
                     radioButton.text, Toast.LENGTH_SHORT
                 ).show()*/
                rgAns.clearCheck()
            } else {
                // no question select
            }
            nextQuestion()

        }
        tvPrev.setOnClickListener {
            previousQuestion()
        }
        rgAns.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.first) {
                questionList[selectedQuestionIndex].selectedAns =
                    KEY_OPTION_1
            } else if (checkedId == R.id.second) {
                questionList[selectedQuestionIndex].selectedAns =
                    KEY_OPTION_2
            } else if (checkedId == R.id.third) {
                questionList[selectedQuestionIndex].selectedAns =
                    KEY_OPTION_3
            } else if (checkedId == R.id.four) {
                questionList[selectedQuestionIndex].selectedAns =
                    KEY_OPTION_4
            }
        }

        tvSubmitQuiz.setOnClickListener {
            questionList.forEach {
                Timber.tag("Quiz")
                    .d("question-->" + it.question + "----/n" + "ans---->" + it.selectedAns)

            }
            submitQuiz()
        }
    }

    private fun initUi() {
        startTimer()
        initQuestion()
    }

    private fun initQuestion() {
        setQuestionTitle()
        val quiz = questionList[selectedQuestionIndex]
        val question = StringBuilder()
        question.append(getQuestionNo())
        question.append(".")
        question.append(" ")
        question.append(quiz.question)
        tvQuestion.text = question.toString()
        first.text = quiz.option1
        second.text = quiz.option2
        third.text = quiz.option3
        four.text = quiz.option4
        /* if (quiz.diagram != null && quiz.diagram.isNotEmpty()) {
             Glide.with(this).load(quiz.diagram).into(ivDiagram)
             ivDiagram.visibility = View.VISIBLE
         } else {
             ivDiagram.visibility = View.GONE
         }*/

        updateButtonVisibility()
    }

    private fun setQuestionTitle() {
        val title = "Question(${getQuestionNo()}/${questionList.size})"
        tvNoOfQuesions.text = title
    }

    private fun getQuestionNo(): String {
        return (selectedQuestionIndex + 1).toString()
    }

    private fun nextQuestion() {
        if (selectedQuestionIndex < questionList.size - 1) {
            selectedQuestionIndex++
        }
        initQuestion()
    }

    private fun previousQuestion() {
        if (selectedQuestionIndex > 0) {
            selectedQuestionIndex--
        }
        initQuestion()

    }

    private fun updateButtonVisibility() {
        if (selectedQuestionIndex == 0) {
            tvPrev.visibility = View.GONE
        } else {
            tvPrev.visibility = View.VISIBLE
        }
        if (selectedQuestionIndex == questionList.size - 1) {
            tvNext.visibility = View.GONE
        } else {
            tvNext.visibility = View.VISIBLE
        }
    }

    private fun startTimer() {
        object : CountDownTimer(quizTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTimer.text = String.format(
                    "%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )

            }

            override fun onFinish() {
                tvTimer.text = "Time Out!"
                submitQuiz()
            }
        }.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            DialogUtils(this).showDialog(
                "Are you sure want to quit Quiz?",
                object : DialogUtils.DialogButtonListener {
                    override fun onNegativeButtonClick() {
                    }

                    override fun onPositiveButtonClick() {
                        finish()
                    }
                })
            // onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getQuizList(quizId: String) {
        progressDialog.show()
        /* val user = PrefrenceData.getUserInfo(this)
         val id = user?.id.toString()
         viewModel.getQuizList(id, quizId)?.observe(this, androidx.lifecycle.Observer {
             progressDialog.dismiss()
             if (it.isSuccessful) {
                 questionList = it.body as ArrayList<Quiz>
                 initUi()
             }
         })*/
    }

    private fun submitQuiz() {
        //  val user = PrefrenceData.getUserInfo(this)
        val id = "" //user?.id.toString()
        val submitQuestions = ArrayList<QuestionIdOptionSelected>()
        questionList.forEach {
            if (it.selectedAns != null) {
                submitQuestions.add(
                    QuestionIdOptionSelected(
                        it.selectedAns,
                        it.id
                    )
                )
            } else {
                submitQuestions.add(
                    QuestionIdOptionSelected(
                        "",
                        it.id
                    )
                )
            }
        }
        val submitQuestionRequestModel =
            SubmitQuestionRequestModel(
                id.toInt(),
                quizId.toInt(),
                submitQuestions
            )
        progressDialog.show()
       /* viewModel.submitQuiz(submitQuestionRequestModel)
            ?.observe(this, androidx.lifecycle.Observer {
                progressDialog.dismiss()
                *//*  if (it.isSuccessful) {
                      val body = it.body
                      if (body != null) {
                          if (body.IsQuizSubmit) {
                              Toast.makeText(this, body.msg, Toast.LENGTH_SHORT).show()
                              finish()
                          } else {
                              Toast.makeText(this, body.msg, Toast.LENGTH_SHORT).show()
                          }
                      }
                  }*//*
            })*/

    }
}