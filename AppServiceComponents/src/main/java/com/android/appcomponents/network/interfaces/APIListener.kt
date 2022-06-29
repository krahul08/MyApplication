package com.android.appcomponents.network.interfaces

import okhttp3.ResponseBody

interface APIListener {
    fun onStarted()
    fun onSuccessResponse(responseBody: ResponseBody)
    fun onErrorResponse(errorMessage: String)
}