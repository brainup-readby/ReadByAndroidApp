package com.brainup.readbyapp.library

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.library.LibTopicListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.InitializQuizActivity
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui.QuizActivityNew
import com.brainup.readbyapp.com.brainup.readbyapp.topics.TopicListActivity
import com.brainup.readbyapp.com.brainup.readbyapp.utils.NewPlayerActivity
import com.brainup.readbyapp.com.brainup.readbyapp.utils.PlayerActivity
import com.brainup.readbyapp.utils.ViewDocumentHelper
import kotlinx.android.synthetic.main.activity_lib_topic_list.*

class LibTopicListActivity : BaseActivity() {
    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_SELECTED_TOPIC = "keySelectedTopic"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib_topic_list)
        title = intent.getStringExtra(TopicListActivity.KEY_TITLE)
        val selectedSubject =
            intent.getSerializableExtra(TopicListActivity.KEY_SELECTED_TOPIC) as UserSelectedChapters
        //   tvStudentName.text = PrefrenceData.getUserName(this)
        rvLibTopics.adapter = LibTopicListAdapter(
            selectedSubject.MAS_TOPICS,
            TopicHandler(this)
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    inner class TopicHandler(val context: Context) {
        fun onItemClick(selectedTopics: UserSelectedTopics) {
            val intent = Intent(context, NewPlayerActivity::class.java)
            intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
            intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
            intent.putExtra(PlayerActivity.TOPIC_ID, ""+selectedTopics.TOPIC_ID)
            context.startActivity(intent)
            //Toast.makeText(context, vendorDealModel.title + "clicked", Toast.LENGTH_SHORT).show()
        }

        fun onPdfButtonClick(selectedTopics: UserSelectedTopics) {
            if (selectedTopics.BOOK_URL != null) {
                ViewDocumentHelper.navigateToCustomTab(context, selectedTopics.BOOK_URL)
            }
        }

        fun onCardClick(selectedTopics: UserSelectedTopics) {
            if (selectedTopics.TOPIC_ID != null  && selectedTopics.IS_ACTIVE.equals("T")) {
               /* val intent = Intent(this@LibTopicListActivity, InitializQuizActivity::class.java)
                intent.putExtra(QuizActivityNew.KEY_SELECTED_TOPIC,selectedTopics)
                startActivity(intent)*/
                // Toast.makeText(this@TopicListActivity,"call"+selectedTopics.TOPIC_ID,Toast.LENGTH_LONG).show()

            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            // onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}