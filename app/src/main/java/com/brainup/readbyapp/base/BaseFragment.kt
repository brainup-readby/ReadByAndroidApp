package com.mj.elearning24.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.Fragment
import com.brainup.readbyapp.R
import dmax.dialog.SpotsDialog


open class BaseFragment : Fragment() {

    protected var activity: Activity? = null
    lateinit var progressDialog: AlertDialog

    fun initProgressbar(context: Context) {
        //  progressDialog = new SpotsDialog(NewPost.this);
        progressDialog = SpotsDialog.Builder()
            .setTheme(R.style.CustomThmeForDialog)
            .setContext(context)
            .setMessage("Please wait..")
            .setCancelable(false)
            .build()
        //progressDialog.show();
    }


}
