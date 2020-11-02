package com.brainup.readbyapp.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brainup.readbyapp.R
import com.brainup.readbyapp.com.brainup.readbyapp.auth.adapter.CourseListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.adapter.SubjectListAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.auth.model.Course
import com.brainup.readbyapp.com.brainup.readbyapp.auth.registration.RegistrationActivity
import kotlinx.android.synthetic.main.activity_course_choose.*
import kotlinx.android.synthetic.main.common_header.*
import timber.log.Timber

class ChooseCourseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_choose)
        headerTitle.text = "Choose"
        iv_logo.visibility = View.GONE
        tvGreetingMsg.text = "your course"
        val courseList = ArrayList<Course>()
        courseList.add(Course("1st"))
        courseList.add(Course("2nd"))
        courseList.add(Course("3rd"))
        courseList.add(Course("4th"))
        courseList.add(Course("5th"))
        courseList.add(Course("6th"))
        courseList.add(Course("7th"))
        courseList.add(Course("8th"))
        courseList.add(Course("9th"))
        courseList.add(Course("10th"))
        courseList.add(Course("11th"))
        courseList.add(Course("12th"))

        rvCourse.adapter = CourseListAdapter(courseList,ClickHandler(this))
    }

    inner class ClickHandler(val context: Context) {
        fun onCourseClick(course: Course) {
            Timber.tag("Click").d(course.courseName)
            context.startActivity(Intent(context, RegistrationActivity::class.java))
        }

    }
}