package com.brainup.readbyapp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

class DialogUtils(private val context: Context) {
    fun showDialog(msg: String, callBack: DialogButtonListener) {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage(msg)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { _, _ ->
            callBack.onPositiveButtonClick()
        }
        //performing cancel action
        /* builder.setNeutralButton("Cancel") { dialogInterface, which ->
             Toast.makeText(applicationContext, "clicked cancel\n operation cancel", Toast.LENGTH_LONG).show()
         }*/
        //performing negative action
        builder.setNegativeButton("No") { _, _ ->
            //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
            callBack.onNegativeButtonClick()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    interface DialogButtonListener {
        fun onPositiveButtonClick()
        fun onNegativeButtonClick()
    }
}