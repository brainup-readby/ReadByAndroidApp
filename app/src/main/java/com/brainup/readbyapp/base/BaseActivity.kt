package com.brainup.readbyapp.base

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity
import com.brainup.readbyapp.R

import dmax.dialog.SpotsDialog

open class BaseActivity : AppCompatActivity() {
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

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


}

