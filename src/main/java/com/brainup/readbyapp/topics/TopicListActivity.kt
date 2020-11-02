package com.brainup.readbyapp.com.brainup.readbyapp.topics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.com.brainup.readbyapp.utils.PlayerActivity
import com.brainup.readbyapp.utils.PrefrenceData
import com.brainup.readbyapp.utils.ViewDocumentHelper
import kotlinx.android.synthetic.main.activity_topic_list.*

class TopicListActivity : AppCompatActivity() {
    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_SELECTED_TOPIC = "keySelectedTopic"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = intent.getStringExtra(KEY_TITLE)
        val selectedSubject =
            intent.getSerializableExtra(KEY_SELECTED_TOPIC) as UserSelectedChapters
        setContentView(R.layout.activity_topic_list)
        tvStudentName.text = "Hi "+ PrefrenceData.getUserName(this)
        rvTopicList.adapter = TopicListAdapter(selectedSubject.MAS_TOPICS, TopicHandler(this))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
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



            val intent = Intent(context, NewPlayerActivity::class.java)
            intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
            intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
            context.startActivity(intent)


           /* val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(PlayerActivity.KEY_TITLE, selectedTopics.TOPIC_NAME)
            intent.putExtra(PlayerActivity.KEY_URL, selectedTopics.VIDEO_URL)
            context.startActivity(intent)*/
            //Toast.makeText(context, vendorDealModel.title + "clicked", Toast.LENGTH_SHORT).show()
        }

        fun onPdfButtonClick(selectedTopics: UserSelectedTopics) {
            if (selectedTopics.BOOK_URL != null) {
                ViewDocumentHelper.navigateToCustomTab(context, selectedTopics.BOOK_URL)
            }
        }
    }
}
