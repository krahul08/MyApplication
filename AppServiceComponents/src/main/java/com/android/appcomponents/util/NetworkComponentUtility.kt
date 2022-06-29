package com.android.appcomponents.util

import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar


class Utility(var context: Context? = null) {

    var ctx = context


    @RequiresApi(Build.VERSION_CODES.M)
    public fun isNetworkConnected(): Boolean {
        val connectivityManager = ctx?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
       // sharedPreferences = ctx?.getSharedPreferences("cordinates", MODE_PRIVATE)!!
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    }

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
}
