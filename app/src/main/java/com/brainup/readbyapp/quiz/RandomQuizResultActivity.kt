package com.brainup.readbyapp.com.brainup.readbyapp.quiz

import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.R
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RandomQuizResponse
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.model.model.RandomQuizModel
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.RandomQuizDisplayAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizResultViewModel
import com.brainup.readbyapp.dashboard.Home
import kotlinx.android.synthetic.main.quiz_activity_list.*
import kotlinx.android.synthetic.main.quiz_activity_list.tvNoOfQuesions
import kotlinx.android.synthetic.main.quiz_result_activity_list.*

class RandomQuizResultActivity : BaseActivity() {

    var layoutManager: LinearLayoutManager? = null
    var values: Int = 1
    var countMaxValues: Int = 1
    var topic_id: Int = 0
    lateinit var name: String
    lateinit var totalQusNo: String
    var lists = ArrayList<RandomQuizResponse>()

    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_RESULT_LIST = "keyResult"
    }
    private lateinit var viewModel: QuizResultViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_result_activity_list)
        initProgressbar(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = intent.getStringExtra(KEY_TITLE)
        viewModel = ViewModelProvider(this).get(QuizResultViewModel::class.java)
        if (title != null && title.isNotEmpty()) {
            this.title = title
        }
        lists = intent.getSerializableExtra(KEY_RESULT_LIST) as ArrayList<RandomQuizResponse>
        if(lists != null && !lists.isEmpty()){
            setUpRandomQuizView(lists)
            quizResultList.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return rv.scrollState == RecyclerView.SCROLL_STATE_DRAGGING
                }
            })
            findViewById<Button>(R.id.tvNextResult).setOnClickListener {
                if (values <= countMaxValues) {
                    values = values.plus(1)
                    tvNoOfQuesions.setText("Question(" + values + totalQusNo)
                }
                if (layoutManager!!.findLastCompletelyVisibleItemPosition() < ((quizResultList.adapter as RandomQuizResultDisplayAdapter).getItemCount() - 1)) {
                    layoutManager!!.scrollToPosition(layoutManager!!.findLastCompletelyVisibleItemPosition() + 1)
                }
            }
            findViewById<Button>(R.id.tvPrevResult).setOnClickListener {
                if (values >= 2) {
                    values = values.minus(1)
                    tvNoOfQuesions.setText("Question(" + values + totalQusNo)
                }
                if (layoutManager!!.findFirstCompletelyVisibleItemPosition() < ((quizResultList.adapter as RandomQuizResultDisplayAdapter).getItemCount() + 1)) {
                    layoutManager!!.scrollToPosition(layoutManager!!.findLastCompletelyVisibleItemPosition() - 1)
                }
            }
        }else{
            Toast.makeText(this, "Data not available. Please check later.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setUpRandomQuizView(body: ArrayList<RandomQuizResponse>) {
        if (body != null) {
            var list = body
            if (list != null) {
                noDataFoundForResult.visibility = View.GONE
                quizResultList.visibility = View.VISIBLE
                countMaxValues = list.size - 1
                setAdapterView(list)
            } else {
                tvSubmitQuiz.isEnabled = false
                tvPrev.isEnabled = false
                tvNext.isEnabled = false
                tvNoOfQuesions.text = "Question(" + "0" + "/0)"
                noDataFoundForResult.visibility = View.VISIBLE
                quizResultList.visibility = View.GONE
            }
        } else {
            tvSubmitQuiz.isEnabled = false
            tvPrev.isEnabled = false
            tvNext.isEnabled = false
            tvNoOfQuesions.text = "Question(" + "0" + "/10)"
            noDataFoundForResult.visibility = View.VISIBLE
            quizResultList.visibility = View.GONE
        }
    }

    private fun setAdapterView(list: ArrayList<RandomQuizResponse>) {
        totalQusNo = "/" + list?.size + ")"
        tvNoOfQuesions.text = "Question(" + values + totalQusNo
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        quizResultList.layoutManager = layoutManager
        quizResultList.adapter = RandomQuizResultDisplayAdapter(list, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun setPositionOfQus(position: Int) {}
}
