package com.example.myapplication.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.appcomponents.network.interfaces.VolleyAPIListener
import com.android.appcomponents.network.interfaces.VolleyListener
import com.android.appcomponents.viewmodel.NetworkAPIViewModel
import com.android.appcomponents.viewmodel.NetworkAPIViewModelFactory
import com.example.myapplication.fragment.VolleyFragment
import com.example.myapplication.utils.Constant
import kotlinx.coroutines.launch


class VolleyViewModel(ctx: Fragment) : ViewModel() {

    class VolleyViewModelFactory(private val volleyFragment: VolleyFragment) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VolleyViewModel(ctx = volleyFragment) as T
        }
    }

    private val networkAPIViewModel: NetworkAPIViewModel = ViewModelProvider(
        ctx.requireActivity(),
        NetworkAPIViewModelFactory(Constant.BASE_URL, ctx.requireContext())
    ).get(NetworkAPIViewModel::class.java)

    lateinit var apiListener: VolleyAPIListener

    fun getDataFromServer() {
        apiListener.onStarted()
        viewModelScope.launch {
            Log.d("Rahul-Volley", "Scope Launch")
            val volleyInstance = networkAPIViewModel.getVolleyClient()
            Log.d("Rahul-Volley", "volleyInstance")
            volleyInstance.getVolleyRequest(Constant.BASE_URL, "todos",
                object : VolleyListener<String, String> {
                    override fun getResult(response: String) {
                        Log.d("Rahul-Volley", "volleyResult")
                        apiListener.onSuccessResponse(response)
                    }
                })
        }
    }
}