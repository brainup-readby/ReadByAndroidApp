package com.brainup.readbyapp.com.brainup.readbyapp.topics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.MainActivity
import com.brainup.readbyapp.MainActivity.Companion.chapter_id
import com.brainup.readbyapp.MainActivity.Companion.subject_id
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserData
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.HomeViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.InitializQuizActivity
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.QuizActivityNew
import com.brainup.readbyapp.com.brainup.readbyapp.utils.NewPlayerActivity
import com.brainup.readbyapp.com.brainup.readbyapp.utils.PlayerActivity
import com.brainup.readbyapp.utils.PrefrenceData
import com.brainup.readbyapp.utils.ViewDocumentHelper
import kotlinx.android.synthetic.main.activity_topic_list.*

class TopicListActivity : BaseActivity() {
    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_SELECTED_TOPIC = "keySelectedTopic"
    }
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent.getStringExtra(KEY_TITLE)
        val selectedSubject = intent.getSerializableExtra(KEY_SELECTED_TOPIC) as UserSelectedChapters
        setContentView(R.layout.activity_topic_list)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        tvStudentName.text = "Hi "+PrefrenceData.getUserName(this)
        //rvTopicList.adapter = TopicListAdapter(selectedSubject.MAS_TOPICS, TopicHandler(this))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initProgressbar(this)
    }
    override fun onResume() {
        super.onResume()
         getUserDetails()
    }

    private fun getUserDetails() {
        progressDialog.show()
        viewModel.getUserDetails(PrefrenceData.getUserId(this))
            ?.observe(TopicListActivity@ this, Observer {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    val body = it.body
                   var userData = body?.data
                   val  list = setData(userData)
                    var listEnable =   getEnableDesableObjectList(list)
                    rvTopicList.adapter = TopicListAdapter(listEnable, TopicHandler(this))
                    //Toast.makeText(this, body?.data?.USER_ID.toString(), Toast.LENGTH_SHORT).show()
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

    inner class TopicHandler(val context: Context) {
        fun onItemClick(selectedTopics: UserSelectedTopics) {

            if(selectedTopics.isEnable){

            if(selectedTopics.VIDEO_STATUS==null ||selectedTopics.VIDEO_STATUS?.equals("p")){
                val intent = Intent(context, NewPlayerActivity::class.java)
                intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
                intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
                intent.putExtra(PlayerActivity.TOPIC_ID, ""+selectedTopics.TOPIC_ID)
                context.startActivity(intent)
            }else{
                Toast.makeText(this@TopicListActivity,"you have already  seen  this  topic video ",Toast.LENGTH_LONG).show()
            }

            }


            //Toast.makeText(context, vendorDealModel.title + "clicked", Toast.LENGTH_SHORT).show()
        }

        fun onPdfButtonClick(selectedTopics: UserSelectedTopics) {
            if(selectedTopics.isEnable){

            if (selectedTopics.BOOK_URL != null) {
                ViewDocumentHelper.navigateToCustomTab(context, selectedTopics.BOOK_URL)
            }

            }
        }


        fun onCardClick(selectedTopics: UserSelectedTopics) {
            if(selectedTopics.isEnable){
            MainActivity.topic_id = selectedTopics.TOPIC_ID.toString()
            if (selectedTopics.TOPIC_ID != null  && selectedTopics.VIDEO_STATUS.equals("c")) {
                if(selectedTopics.TEST_STATUS.equals("p")){
                    val intent = Intent(this@TopicListActivity, InitializQuizActivity::class.java)
                    intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC,selectedTopics)
                    startActivity(intent)
                } else if(selectedTopics.TEST_STATUS.equals("c")) {
                    Toast.makeText(this@TopicListActivity,"You have  already done video and test ",Toast.LENGTH_LONG).show()

                }
            }else{
                Toast.makeText(this@TopicListActivity,"Please complete video  first  ",Toast.LENGTH_LONG).show()
            }

            }
          /*  val intent = Intent(this@TopicListActivity, InitializQuizActivity::class.java)
            intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC,selectedTopics)
            startActivity(intent)*/


        }
    }
}

    private fun getEnableDesableObjectList(userData: List<UserSelectedTopics>?): List<UserSelectedTopics>? {
         for (item in userData!!){
             if(item.VIDEO_STATUS==null){
                 item.isEnable = true
                 return userData
             }

             if(item.VIDEO_STATUS.equals("p")){
                 item.isEnable = true
                 return userData
             }


         }
        return userData
    }


private fun setData(
    userData: UserData?
): List<UserSelectedTopics>? {
    var  list  =  arrayListOf<UserSelectedTopics>()
    if(userData!!.USER_SUBSCRIPTION[0].MAS_STREAM !=null){
        var   streemModel =  userData!!.USER_SUBSCRIPTION[0].MAS_STREAM
        var listOfsubject=   streemModel.MAS_SUBJECT
        for (item in  listOfsubject){
            var  id  =  item.SUBJECT_ID
            if(id==subject_id.toInt()){
                var listofchapter =  item.MAS_CHAPTERS
                for (chapterItem in  listofchapter){
                    var  id  =  chapterItem.CHAPTER_ID
                    if(id== chapter_id.toInt()){
                        var   listofTopic   =  chapterItem.MAS_TOPICS
                        list.addAll(listofTopic)
                    }
                }
            }
        }

    }
    return  list

}

