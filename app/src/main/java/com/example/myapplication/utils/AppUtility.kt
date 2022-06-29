package com.example.myapplication.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(activity: Activity, message: String, action: String? = null,
                 actionListener: View.OnClickListener? = null, duration: Int = Snackbar.LENGTH_INDEFINITE) {
    val snackBar = Snackbar.make(activity.findViewById(android.R.id.content), message, duration)
        //.setBackgroundTint(Color.parseColor("#CC000000")) //
        .setTextColor(Color.WHITE)
    if (action != null && actionListener!=null) {
        snackBar.setAction(action, actionListener)
    }
    snackBar.show()
}