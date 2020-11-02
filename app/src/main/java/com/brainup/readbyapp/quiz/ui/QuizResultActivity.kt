package com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.databinding.ActivityQuizResultBinding
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizResultViewModel
import com.brainup.readbyapp.quiz.model.model.DataX
import com.brainup.readbyapp.quiz.model.model.TestRequestModel
import timber.log.Timber

class QuizResultActivity : BaseActivity() {
    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_RESULT = "keyResult"
    }
    private lateinit var viewModel: QuizResultViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityQuizResultBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_quiz_result
        )
        initProgressbar(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = intent.getStringExtra(KEY_TITLE)
        viewModel = ViewModelProvider(this).get(QuizResultViewModel::class.java)
        if (title != null && title.isNotEmpty()) {
            this.title = title
        }
        val data = intent.getSerializableExtra(KEY_RESULT)
        if (data != null) {
            val quizResult = data as DataX
            if (quizResult != null) {
                val data = MutableLiveData<DataX>()
                data.postValue(quizResult)
                viewModel.setResult(data)
                viewModel.getResult()?.observe(this, Observer {
                    Timber.d("Test-->${it.TOTAL_MARKS_OBTAINED}")
                    binding.resultQuiz = viewModel
                })

                if(quizResult.OVERALL_RESULT ==null || quizResult.OVERALL_RESULT.equals("fail")){
                    var   model   =    TestRequestModel(
                        "p",quizResult.TOPIC_ID
                    )
                    submitStatus(model)
                }else{
                    var   model   =    TestRequestModel(
                        "c",quizResult.TOPIC_ID
                    )
                    submitStatus(model)
                }



            }
        }


        // val quizResult = QuizResult(10, 4, 5, 6, 6, 2)

    }



    private fun submitStatus(lists: TestRequestModel) {
        viewModel.updateTopicFlag(lists)
            ?.observe(QuizActivityNew@ this, Observer {
                if (it.isSuccessful) {

                }else{
                    Toast.makeText(this,"please select answer", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            // onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
