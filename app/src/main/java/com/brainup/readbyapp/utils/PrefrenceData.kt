package com.brainup.readbyapp.utils

import android.content.Context
import com.brainup.readbyapp.com.brainup.readbyapp.utils.extensions.SharedPrefsManager


/**
 * Created by Manish jain on 26-04-2017.
 */

object PrefrenceData {
    private const val CLASS_DATA = "classInfo"
    private const val SCHOOLS_DATA = "schoolInfo"
    private const val SUBJECT_INFO = "subjectInfo"
    private const val APP_SETTINGS = "App_settings"
    private const val STUDENT_INFO = "studentInfo"
    private const val PRIVATE_MODE = 0
    private const val USER_LOGED_IN = "userLoggedIn"
    private const val USER_MOB_NO = "keyMobNo"
    private const val USER_USER_ID = "keyUserID"
    private const val USER_NAME = "keyUserName"
    private const val EMAIL_ID = "email_id"
    private const val VIDEO_STATUS = "video_status"
    private const val TEST_STATUS = "test_status"
    /*   fun saveClassInfo(userModel: List<ClassModel>?, context: Context) {
           val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
           val prefsEditor = shared.edit()
           if (userModel != null) {
               val gson = Gson()
               val json = gson.toJson(userModel) // myObject - instance of MyObject
               prefsEditor.putString(CLASS_DATA, json)
           } else {
               prefsEditor.putString(CLASS_DATA, null)
           }
           prefsEditor.commit()
       }

       fun getClassInfo(context: Context): List<ClassModel>? {
           val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
           val gson = Gson()
           val json = shared.getString(CLASS_DATA, "")
           val type = object : TypeToken<List<ClassModel>>() {

           }.type
           return gson.fromJson(json, type)
       }

       fun saveSchoolInfo(userModel: List<SchoolModel>?, context: Context) {
           val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
           val prefsEditor = shared.edit()
           if (userModel != null) {
               val gson = Gson()
               val json = gson.toJson(userModel) // myObject - instance of MyObject
               prefsEditor.putString(SCHOOLS_DATA, json)
           } else {
               prefsEditor.putString(SCHOOLS_DATA, "")
           }
           prefsEditor.commit()
       }

       fun getSchoolInfo(context: Context): List<SchoolModel>? {
           val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
           val gson = Gson()
           val json = shared.getString(SCHOOLS_DATA, "")
           val type = object : TypeToken<List<SchoolModel>>() {

           }.type
           return gson.fromJson(json, type)
       }

       fun saveSubjectInfo(userModel: List<SubjectModel>?, context: Context) {
           val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
           val prefsEditor = shared.edit()
           if (userModel != null) {
               val gson = Gson()
               val json = gson.toJson(userModel) // myObject - instance of MyObject
               prefsEditor.putString(SUBJECT_INFO, json)
           } else {
               prefsEditor.putString(SUBJECT_INFO, "")
           }
           prefsEditor.commit()
       }

       fun getSubjectInfo(context: Context): List<SubjectModel>? {
           val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
           val gson = Gson()
           val json = shared.getString(SUBJECT_INFO, "")
           val type = object : TypeToken<List<SubjectModel>>() {

           }.type
           return gson.fromJson(json, type)
       }
       */
/*    fun saveUserInfo(userModel: LoginResponse?, context: Context) {
        val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
        val prefsEditor = shared.edit()
        if (userModel != null) {
            val gson = Gson()
            val json = gson.toJson(userModel) // myObject - instance of MyObject
            prefsEditor.putString(STUDENT_INFO, json)
        } else {
            prefsEditor.putString(STUDENT_INFO, "")
        }
        prefsEditor.commit()
    }*/

    /*  fun getUserInfo(context: Context): LoginResponse? {
          val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
          val gson = Gson()
          val json = shared.getString(STUDENT_INFO, "")
          val type = object : TypeToken<LoginResponse>() {
          }.type
          return gson.fromJson(json, type)
      }*/

    fun clearAllData(context: Context) {
        val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
        val prefsEditor = shared.edit()
        prefsEditor.clear()
        prefsEditor.commit()
    }

    fun saveKeepMeLogin(context: Context, isLogin: Boolean) {
        val shared = context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE)
        val prefsEditor = shared.edit()
        prefsEditor.putBoolean(USER_LOGED_IN, isLogin)
        prefsEditor.commit()
    }

    fun isUserLoggedIn(context: Context): Boolean {
        return SharedPrefsManager.newInstance(context).getBoolean(USER_LOGED_IN, false)
    }

    fun setUserLogin(context: Context) {
        SharedPrefsManager.newInstance(context).putBoolean(USER_LOGED_IN, true)
    }

    fun setUserLoginFromLogout(context: Context,  isLogin : Boolean) {
        SharedPrefsManager.newInstance(context).putBoolean(USER_LOGED_IN, isLogin)
    }

    fun setUserId(context: Context, userID: String) {
        SharedPrefsManager.newInstance(context).putString(USER_USER_ID, userID)
    }

    fun getMobNo(context: Context): String {
        return SharedPrefsManager.newInstance(context).getString(USER_MOB_NO, "")!!
    }

    fun setMobNo(context: Context, mobNo: String) {
        return SharedPrefsManager.newInstance(context).putString(USER_MOB_NO, mobNo)
    }

    fun getEmailID(context: Context): String {
        return SharedPrefsManager.newInstance(context).getString(EMAIL_ID, "")!!
    }

    fun setEmailID(context: Context, emailID: String) {
        return SharedPrefsManager.newInstance(context).putString(EMAIL_ID, emailID)
    }


    fun getUserId(context: Context): String {
        return SharedPrefsManager.newInstance(context).getString(USER_USER_ID, "")!!
    }

    fun setUserName(context: Context, mob: String) {
        SharedPrefsManager.newInstance(context).putString(USER_NAME, mob)
    }

    fun getUserName(context: Context): String {
        return SharedPrefsManager.newInstance(context).getString(USER_NAME, "")!!
    }

    fun getVideoStatus(context: Context,topicId:String): String {
        return SharedPrefsManager.newInstance(context).getString(VIDEO_STATUS+"_"+topicId, "")!!
    }

    fun setVideoStatus(context: Context, videoStatus: String, topicId:String) {
        return SharedPrefsManager.newInstance(context).putString(VIDEO_STATUS+"_"+topicId, videoStatus)
    }

    fun getTestStatus(context: Context, topicId:String): String {
        return SharedPrefsManager.newInstance(context).getString(TEST_STATUS+"_"+topicId, "")!!
    }

    fun setTestStatus(context: Context, testStatus: String, topicId:String) {
        return SharedPrefsManager.newInstance(context).putString(TEST_STATUS+"_"+topicId, testStatus)
    }
}
