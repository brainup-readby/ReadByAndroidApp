package com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.registration.BoardData
import com.brainup.readbyapp.auth.registration.Course
import com.brainup.readbyapp.auth.registration.Stream
import com.brainup.readbyapp.auth.registration.Year
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.adapter.CustomArrayAdapter
import com.brainup.readbyapp.com.brainup.readbyapp.auth.model.ModelDisplayName
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.DashBoardActivity
import com.brainup.readbyapp.utils.PrefrenceData
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_choose_academic.*
import kotlinx.android.synthetic.main.common_header.*

class ChooseAcademicActivity : BaseActivity() {
    private lateinit var viewModel: ChooseAcademicViewModel
    private var boards: List<BoardData>? = null
    private var courseData: HashMap<Long, List<Course>> = HashMap()
    private lateinit var outlineBoard: TextInputLayout
    private lateinit var outlineCourse: TextInputLayout
    private lateinit var outlineStream: TextInputLayout
    private lateinit var outlineYear: TextInputLayout
    private lateinit var outlineInstitute: TextInputLayout
    private lateinit var courseListAdapter: CustomArrayAdapter
    private lateinit var streamListAdapter: CustomArrayAdapter
    private lateinit var yearListAdapter: CustomArrayAdapter
    var courseListItems = ArrayList<ModelDisplayName>()
    var streamListItems = ArrayList<ModelDisplayName>()
    var yearListItems = ArrayList<ModelDisplayName>()
    private var boardId: Long? = 0
    private var courseId: Long? = 0
    private var streamId: Long? = 0
    private var yearId: Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_academic)
        headerTitle.text = "Choose"
        tvGreetingMsg.text = "your exam"
        initUi()
        courseListAdapter = bindCustomAdapter(courseListItems)
        streamListAdapter = bindCustomAdapter(streamListItems)
        yearListAdapter = bindCustomAdapter(yearListItems)
        spinnerCourseName.setAdapter(courseListAdapter)
        spinnerStream.setAdapter(streamListAdapter)
        spinnerClassYear.setAdapter(yearListAdapter)
        viewModel = ViewModelProvider(this).get(ChooseAcademicViewModel::class.java)
        getBoardData()
        viewModel.academicFormState.observe(this, Observer {
            if (it.boardNameError != null) {
                showError(getString(it.boardNameError))
                //outlineCourse.error = getString(it.boardNameError)
            }
            if (it.courseNameError != null) {
                showError(getString(it.courseNameError))
                // outlineCourse.error = getString(it.courseNameError)
            }
            if (it.streamNameError != null) {
                //outlineStream.error = getString(it.streamNameError)
                showError(getString(it.streamNameError))
            }
            if (it.yearNameError != null) {
                //  outlineYear.error = getString(it.yearNameError)
                showError(getString(it.yearNameError))
            }
            if (it.instituteNameError != null) {
                // outlineInstitute.error = getString(it.instituteNameError)
                showError(getString(it.instituteNameError))
            }
            if (it.isDataValid) {
                //  Toast.makeText(this, "call api", Toast.LENGTH_SHORT).show()
                postSubscription()
            }
        })
        btnDone.setOnClickListener {
            //val intent = Intent(this, DashBoardActivity::class.java)
            //startActivity(intent)
            viewModel.checkValidation(
                spinnerBoard.text.toString(),
                spinnerCourseName.text.toString(),
                spinnerStream.text.toString(),
                spinnerClassYear.text.toString(),
                etInstituteName.text.toString()
            )
            //  postSubscription()
        }

    }

    private fun showError(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
    }

    private fun initUi() {
        initProgressbar(this)
        outlineBoard = txtInputBoard
        outlineCourse = txtInputCourse
        outlineStream = txtInputStream
        outlineYear = txtInputYear
        outlineInstitute = txtInputInstitute
    }

    private fun bindCustomAdapter(list: ArrayList<ModelDisplayName>): CustomArrayAdapter {
        return CustomArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            R.id.tvDropDown,
            list
        )
    }


    private fun getBoardData() {
        progressDialog.show()
        viewModel.getAllCourse()?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                boards = it.body?.data
               // Toast.makeText(this, boards?.get(0)?.BOARD_NAME, Toast.LENGTH_SHORT).show()
                setBoardData()
                /*   spinnerBoard.setAdapter(bindCustomAdapter(boardArray))
                   spinnerCourseName.setAdapter(bindCustomAdapter(schoolArray))
                   spinnerStream.setAdapter(bindCustomAdapter(streamArray))
                   spinnerClassYear.setAdapter(bindCustomAdapter(yearArray))*/
            }
        })
    }

    private fun setBoardData() {
        if (!boards.isNullOrEmpty()) {
            var boardList = ArrayList<ModelDisplayName>()
            boards?.forEach {

                if(it.BOARD_NAME !=null  &&  it.BOARD_NAME.length>0){
                    boardList.add(
                        ModelDisplayName(
                            title = it.BOARD_NAME,
                            id = it.BOARD_ID
                        )
                    )
                }else{
                    boardList.add(
                        ModelDisplayName(
                            title = "Untitled",
                            id = it.BOARD_ID
                        )
                    )
                }


                courseData[it.BOARD_ID] = it.MAS_COURSE
            }
            //boardListAdapter=bindCustomAdapter(boardList)
            spinnerBoard.setAdapter(bindCustomAdapter(boardList))
            spinnerBoard.setOnItemClickListener { parent, view, position, id ->
                boardId = boards?.get(position)?.BOARD_ID
                // clearError(txtInputBoard)
                setCourseData(boardId)
                //val modelDisplayName = boards?.get(position)?.BOARD_NAME
                //Toast.makeText(view?.context, modelDisplayName, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setCourseData(boardId: Long?) {

   if (!courseData.isNullOrEmpty()){
        val courseList = courseData[boardId]
        courseListItems.clear()
        courseList?.forEach {

            if(it.COURSE_NAME !=null &&  it.COURSE_NAME.length>0){
                courseListItems.add(
                    ModelDisplayName(
                        it.COURSE_NAME,
                        it.COURSE_ID
                    )
                )
            }else{
                courseListItems.add(
                    ModelDisplayName(
                        "Test",
                        it.COURSE_ID
                    )
                )
            }

        }
        courseListAdapter.notifyDataSetChanged()
        /* if (::courseListAdapter.isInitialized) {
             if (courseListItems.size == 0) {
                 courseListAdapter.clear()
                 spinnerCourseName.setText("")
                 spinnerStream.setText("")
                 spinnerClassYear.setText("")
             }
             courseListAdapter.notifyDataSetChanged()
         } else {
             courseListAdapter = bindCustomAdapter(courseListItems)
             spinnerCourseName.setAdapter(courseListAdapter)
         }*/
        spinnerCourseName.setOnItemClickListener { parent, view, position, id ->
            val id = courseListItems[position].id
            clearError(txtInputCourse)
            val selectedCourse = courseList?.find { it.COURSE_ID == id }
            courseId = selectedCourse?.COURSE_ID
            setStreamData(selectedCourse?.MAS_STREAM)
            setYearData(selectedCourse?.MAS_COURSE_YEAR)

        }
    }
    }

    private fun setStreamData(streamList: List<Stream>?) {
        streamListItems.clear()
        streamList?.forEach {
            if( it.STREAM_NAME !=null &&   it.STREAM_NAME.length>0){
                streamListItems.add(
                    ModelDisplayName(
                        it.STREAM_NAME,
                        it.STREAM_ID
                    )
                )
            }else{
                streamListItems.add(
                    ModelDisplayName(
                       "Test",
                        it.STREAM_ID
                    )
                )
            }


        }
        streamListAdapter.notifyDataSetChanged()
        /* if (::streamListAdapter.isInitialized) {
             if (streamListItems.size == 0) {
                 spinnerStream.setText("")
             }
             spinnerStream.setAdapter(streamListAdapter)
         } else {
             streamListAdapter = bindCustomAdapter(streamListItems)
             spinnerStream.setAdapter(streamListAdapter)
         }*/
        spinnerStream.setOnItemClickListener { parent, view, position, id ->
            streamId = streamListItems[position].id
            // clearError(txtInputStream)
            //val modelDisplayName = boards?.get(position)?.BOARD_NAME
            //Toast.makeText(view?.context, modelDisplayName, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setYearData(yearList: List<Year>?) {
        yearListItems.clear()
        yearList?.forEach {

            if( it.DISPLAY_NAME!=null &&   it.DISPLAY_NAME.length>0){
                yearListItems.add(
                    ModelDisplayName(
                        it.DISPLAY_NAME,
                        it.YEAR_ID
                    )
                )
            }else{
                yearListItems.add(
                    ModelDisplayName(
                        "Test",
                        it.YEAR_ID
                    )
                )
            }

        }
        yearListAdapter.notifyDataSetChanged()
        spinnerClassYear.setOnItemClickListener { parent, view, position, id ->
            yearId = yearListItems[position].id
            // clearError(txtInputStream)
            //val modelDisplayName = boards?.get(position)?.BOARD_NAME
            //Toast.makeText(view?.context, modelDisplayName, Toast.LENGTH_SHORT).show()
        }
        /* if (::yearListAdapter.isInitialized) {
             if (yearListItems.size == 0) {
                 spinnerStream.setText("")
             }
             spinnerClassYear.setAdapter(yearListAdapter)
         } else {
             yearListAdapter = bindCustomAdapter(yearListItems)
             spinnerClassYear.setAdapter(yearListAdapter)
         }*/
    }

    private fun postSubscription() {
        progressDialog.show()
        val body =
            SubscriptionModel(
                BOARD_ID = boardId,
                COURSE_ID = courseId,
                STREAM_ID = streamId,
                YEAR_ID = yearId,
                MOBILE_NO = PrefrenceData.getMobNo(this),
                INSTITUTION_NAME = etInstituteName.text.toString()
            )
        viewModel.subscribePlan(body)?.observe(this, Observer {
            progressDialog.dismiss()
            if (it.isSuccessful) {
                val body = it.body
                PrefrenceData.setUserLogin(this)
                val intent = Intent(this, DashBoardActivity::class.java)
                startActivity(intent)
                finish()
                // Toast.makeText(this, it.body?.data?.DEVICE_ID, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearError(inputTextInputLayout: TextInputLayout) {
        inputTextInputLayout.error = null
    }

}


