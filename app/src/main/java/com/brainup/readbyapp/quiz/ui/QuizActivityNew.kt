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
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizViewModel
import com.brainup.readbyapp.payment.model.Data
import com.brainup.readbyapp.payment.model.InitiatePaymentRequest
import com.brainup.readbyapp.quiz.model.model.QusRequestModel
import com.brainup.readbyapp.quiz.model.model.QusRequestModelItem
import com.brainup.readbyapp.quiz.model.model.RBMULTIPLEOPTION
import com.brainup.readbyapp.quiz.model.model.RBQUESTIONS
import com.brainup.readbyapp.utils.PrefrenceData

import kotlinx.android.synthetic.main.quiz_activity_list.*


class QuizActivityNew : BaseActivity() {
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
        setContentView(R.layout.quiz_activity_list)
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        initProgressbar(this)
         quizList.addOnItemTouchListener(object : SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return rv.scrollState == RecyclerView.SCROLL_STATE_DRAGGING
            }
        })
        findViewById<Button>(R.id.tvNext).setOnClickListener {
             if(values<=countMaxValues){
                 values  = values.plus(1)
                 tvNoOfQuesions.setText("Question("+values+totalQusNo)
             }
            if (layoutManager!!.findLastCompletelyVisibleItemPosition() < ( (quizList.adapter as QuizListAdapter).getItemCount() - 1)) {
                layoutManager!!.scrollToPosition(layoutManager!!.findLastCompletelyVisibleItemPosition() + 1);
            }

        }

        findViewById<Button>(R.id.tvPrev).setOnClickListener {
            if(values>=2){
                values  = values.minus(1)
                tvNoOfQuesions.setText("Question("+values+totalQusNo)
            }
            if (layoutManager!!.findFirstCompletelyVisibleItemPosition() < ( (quizList.adapter as QuizListAdapter).getItemCount() + 1)) {
                layoutManager!!.scrollToPosition(layoutManager!!.findLastCompletelyVisibleItemPosition() -1);
            }
        }

        findViewById<TextView>(R.id.tvSubmitQuiz).setOnClickListener {
               var  list  =   (quizList.adapter as QuizListAdapter).getAllListWithSelected()
               var  lists   =   QusRequestModel()
               var   userid   = PrefrenceData.getUserId(this)
                  for (item  in list!!){
                    var  getAnsID =  getAnsId(item.RB_MULTIPLE_OPTION)
                    var   model   =    QusRequestModelItem(
                        userid.toInt(),getAnsID,item.QUESTIONID,topic_id,userid.toInt()
                    )
                      lists.add(model)
                  }
                   var  isFlag=  getApiCallStatus(lists)
                   if(isFlag){
                       submitAns(lists)
                   }else{
                       Toast.makeText(this,"All questions are compulsory",Toast.LENGTH_SHORT).show()
                   }

        }

            val selectedSubject = intent.getSerializableExtra(KEY_SELECTED_TOPIC) as UserSelectedTopics
            getQusList(selectedSubject.TOPIC_ID.toString())
            //getQusList("1")




    }

    private fun getApiCallStatus(lists: QusRequestModel): Boolean {
        var  boolean :Boolean =true
        for (item in lists){
            if(item.GIVEN_ANSWER===0){
                boolean = false
                return  boolean
            }
        }
       return  boolean
    }

    private fun getAnsId(rbMultipleOption: List<RBMULTIPLEOPTION>): Int {
        var  isFlag  :  Int = 0
        for (item in rbMultipleOption){
            if(item.SET_SELECTION){
               isFlag   = item.OPTION_ID
            }
        }
         return isFlag;
    }

    private fun submitAns(lists: QusRequestModel) {

        progressDialog.show()
        viewModel.submitQuestionList(lists)
            ?.observe(QuizActivityNew@ this, Observer {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    val body = it.body
                    val intent = Intent(this, QuizResultActivity::class.java)
                    intent.putExtra(QuizResultActivity.KEY_TITLE,"Results")
                    intent.putExtra(QuizResultActivity.KEY_RESULT,it.body?.data)
                    startActivity(intent)
                   // Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this,"please select answer",Toast.LENGTH_SHORT).show()
                }
            })
    }




    private fun getQusList(id: String) {
        progressDialog.show()
        viewModel.getQuestionList(id)
            ?.observe(QuizActivityNew@ this, Observer {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    val body = it.body
                    var list  =   body?.data?.RB_QUESTIONS
                    if (list != null) {
                        noDataFound.visibility = View.GONE
                        quizList.visibility = View.VISIBLE
                        topic_id = body?.data?.TOPIC_ID!!
                        countMaxValues =  list.size-1
                        setAdapterView(list)
                    }else{
                        tvSubmitQuiz.isEnabled =  false
                        tvPrev.isEnabled =  false
                        tvNext.isEnabled =  false
                        tvNoOfQuesions.setText("Question("+"0"+"/10)")
                        noDataFound.visibility = View.VISIBLE
                        quizList.visibility = View.GONE
                    }
                }
            })

    }

    private fun setAdapterView(list: List<RBQUESTIONS>?) {
         totalQusNo =  "/"+ list?.size +")"
        tvNoOfQuesions.setText("Question("+values+totalQusNo)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        quizList.setLayoutManager(layoutManager)
        quizList.adapter = QuizListAdapter(list,this)
    }

    fun setPositionOfQus(position: Int) {

    }



}