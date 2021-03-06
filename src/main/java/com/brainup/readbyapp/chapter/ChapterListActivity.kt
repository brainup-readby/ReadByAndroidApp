package com.brainup.readbyapp.chapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSelectedSubject
import com.brainup.readbyapp.com.brainup.readbyapp.chapter.ChapterListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.topics.TopicListActivity
import com.brainup.readbyapp.utils.PrefrenceData
import kotlinx.android.synthetic.main.activity_subject_list.*

class ChapterListActivity : AppCompatActivity() {
    companion object {
        const val KEY_TITLE = "keyTitle"
        const val KEY_SELECTED_SUBJECT = "keySelectedSubject"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titleString = intent.getStringExtra(KEY_TITLE)
        title = titleString
        val selectedSubject =
            intent.getSerializableExtra(KEY_SELECTED_SUBJECT) as UserSelectedSubject
        setContentView(R.layout.activity_subject_list)
        tvStudentName.text = PrefrenceData.getUserName(this)
        rvChapterList.adapter =
            ChapterListAdapter(selectedSubject.MAS_CHAPTERS, ChapterClickHandler(this))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        //tvSubjectTitle.text=titleString
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            // onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    inner class ChapterClickHandler(val context: Context) {
        fun onItemClick(userSelectedChapters: UserSelectedChapters) {
            val intent = Intent(context, TopicListActivity::class.java)
            intent.putExtra(TopicListActivity.KEY_TITLE, userSelectedChapters.CHAPTER_NAME)
            intent.putExtra(TopicListActivity.KEY_SELECTED_TOPIC, userSelectedChapters)
            context.startActivity(intent)
            //Toast.makeText(context, vendorDealModel.title + "clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
