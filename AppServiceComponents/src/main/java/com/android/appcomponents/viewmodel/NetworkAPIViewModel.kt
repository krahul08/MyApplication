package com.android.appcomponents.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.appcomponents.util.NetworkUtility
import com.android.appcomponents.util.Utility
import com.android.appcomponents.util.VolleyNetworkManager
import retrofit2.Retrofit

class NetworkAPIViewModel(private val base_url: String, private val context: Context): ViewModel() {
    private val utility = Utility(context)
    @RequiresApi(Build.VERSION_CODES.M)
    fun getNetworkClient(): Retrofit {
        checkInternetConnection()
        return NetworkUtility(context).getRetrofitInstance(base_url)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkInternetConnection() {

        if (!utility.isNetworkConnected()) {
            utility.showSnackBar(context as Activity, "Internet is not Available",
            "ENABLE",
            View.OnClickListener {
                ContextCompat.startActivity(context, Intent(Settings.ACTION_SETTINGS), null)
            })
        }
    }

    fun getVolleyClient(): VolleyNetworkManager {
        return VolleyNetworkManager.getInstance(context)
    }
}

class NetworkAPIViewModelFactory(private val base_url: String, private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NetworkAPIViewModel(base_url, context) as T
    }
}