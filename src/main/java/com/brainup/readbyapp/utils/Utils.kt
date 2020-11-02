package com.brainup.readbyapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.brainup.readbyapp.R
import com.brainup.readbyapp.com.brainup.readbyapp.auth.acadmic.ChooseAcademicActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap


object Utils {

    fun setFilter(editText: EditText) {
        val editFilters = editText.filters
        val newFilters = arrayOfNulls<InputFilter>(editFilters.size + 1)
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.size)
        newFilters[editFilters.size] = InputFilter.AllCaps()
        editText.filters = newFilters
    }

    fun createPartFromString(data: String): RequestBody {
        return RequestBody.create(
            MediaType.parse("text/plain"), data
        )
    }

    val emailRegex = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun optimizeImageToFile(file: File): File? {
        try {
            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image

            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 50

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)

            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            //file.createNewFile()
            //val optimizeFile=
            val outputStream = FileOutputStream(file)

            selectedBitmap!!.compress(Bitmap.CompressFormat.PNG, 60, outputStream)

            return file
        } catch (e: Exception) {
            e.stackTrace
            return null
        }

    }

    //var session: Int = 0
    //var mobileNo = "user name"
    // var loginData: LoginResponse? = null

    @SuppressLint("ObsoleteSdkInt")
    @Throws(Throwable::class)
    fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap?
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(
                    videoPath,
                    HashMap()
                ) else mediaMetadataRetriever.setDataSource(videoPath)
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

    fun isValidEmail(email: String?): Boolean {
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun showServerDownMsg(context: Context) {
        Toast.makeText(
            context,
            "Server is not responding, please try after some time",
            Toast.LENGTH_LONG
        ).show()
    }

    fun openPlayStore(context: Context) {
        val appPackageName: String = context.packageName
        try {
            var intent = Intent()
            val manager: PackageManager = context.packageManager
            intent = manager.getLaunchIntentForPackage(appPackageName)!!
            //launch play store with package name
            val playStoreIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
            context.startActivity(playStoreIntent)
        } catch (activityNotFound: ActivityNotFoundException) {
            // to handle play store not installed scenario
            val intent = Intent(
                Intent.ACTION_VIEW, Uri
                    .parse("http://play.google.com/store/apps/details?id=$appPackageName")
            )
            context.startActivity(intent)
        }
    }

    fun convert_24_hours_to_12_hours(_24HourTime: String): String {
        var convertTime = ""
        try {
            //val _24HourTime = "22:15"
            val _24HourSDF = SimpleDateFormat("HH:mm")
            val _12HourSDF = SimpleDateFormat("hh:mm a")
            val _24HourDt: Date = _24HourSDF.parse(_24HourTime)
            convertTime = _12HourSDF.format(_24HourDt)
            //System.out.println(_24HourDt)
            //System.out.println(_12HourSDF.format(_24HourDt))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return convertTime
    }

    fun hideKeypad(mContext: Context?) {
        if (mContext != null) {
            val activity = mContext as Activity
            val view = activity.currentFocus
            if (view != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    fun showDialog(context: Context) {
        val items = arrayOf("Competitive Exam", "Academic")

        MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle("Choose Course")
            .setItems(items) { dialog, which ->
                // Respond to item chosen
                if (which == 0) {
                    Toast.makeText(context, "Currently not available", Toast.LENGTH_SHORT).show()
                    /* val intent = Intent(this, ChooseCompetitiveActivity::class.java)
                     startActivity(intent)*/
                } else {
                    val intent = Intent(context, ChooseAcademicActivity::class.java)
                    context.startActivity(intent)
                }
                //Toast.makeText(this@LoginActivity, items[which], Toast.LENGTH_SHORT).show()
            }.setCancelable(false)
            .show()
    }
}
