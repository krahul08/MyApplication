package com.android.appcomponents.network.interfaces

interface VolleyAPIListener {
    fun onStarted()
    fun onSuccessResponse(response: String)
    fun onErrorResponse(errorMessage: String)
}