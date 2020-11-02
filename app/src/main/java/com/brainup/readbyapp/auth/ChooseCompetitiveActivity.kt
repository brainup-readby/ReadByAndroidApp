package com.brainup.readbyapp.com.brainup.readbyapp.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brainup.readbyapp.R
import com.brainup.readbyapp.com.brainup.readbyapp.auth.adapter.ExamListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.auth.model.Course
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.DashBoardActivity
import kotlinx.android.synthetic.main.activity_choose_competitive.*
import kotlinx.android.synthetic.main.common_header.*
import timber.log.Timber

class ChooseCompetitiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_competitive)
        headerTitle.text = "Choose"
        tvGreetingMsg.text = "your exam"
        val courseList = ArrayList<Course>()
        courseList.add(Course("Polytechnic Entrance"))
        courseList.add(Course("ITI Entrance"))
        courseList.add(Course("SSC"))
        courseList.add(Course("Junior Engineer"))
        courseList.add(Course("Loco Pilot"))
        courseList.add(Course("Technician Exam"))

        rvCompetitiveExam.adapter = ExamListAdapter(courseList, ClickHandler(this))
    }

    inner class ClickHandler(val context: Context) {
        fun onCourseClick(course: Course) {
            Timber.tag("Click").d(course.courseName)
            context.startActivity(Intent(context, DashBoardActivity::class.java))
        }

    }
}