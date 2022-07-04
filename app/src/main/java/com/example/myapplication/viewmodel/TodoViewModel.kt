package com.example.myapplication.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.appcomponents.network.interfaces.APIListener
import com.android.appcomponents.network.interfaces.NetworkAPI
import com.android.appcomponents.util.Utility
import com.android.appcomponents.viewmodel.NetworkAPIViewModel
import com.android.appcomponents.viewmodel.NetworkAPIViewModelFactory
import com.example.myapplication.utils.Constant
import kotlinx.coroutines.launch

class TodoViewModel(ctx: Fragment): ViewModel() {
    private val context = ctx;
    //Initialise NetworkAPIViewModel from AppComponent Library
    private val networkAPIViewModel: NetworkAPIViewModel = ViewModelProvider(
       ctx.requireActivity(), NetworkAPIViewModelFactory(Constant.BASE_URL, ctx.requireContext())).get(NetworkAPIViewModel::class.java)

    //Initialise API Listener for handling the response
    var apiListener: APIListener? = null

    @RequiresApi(Build.VERSION_CODES.M)
    fun getTodosFromServer() {
        apiListener?.onStarted()
        viewModelScope.launch {
            try {
                //get retrofit instance from library
                val retrofitInstance = networkAPIViewModel.getNetworkClient().create(NetworkAPI::class.java)

                    //Passing params to post request
                    val fieldMap = hashMapOf<String, Any>()
                    fieldMap.put("title", "New Title")
                    fieldMap.put("body", "New BOdy")
                    fieldMap.put("userId", 1)
                //calling POST request
//                val response = retrofitInstance.postRequest("posts", hashMapOf(), fieldMap)

                //calling get request
//                val qeryMap = hashMapOf<String, String>()
//                qeryMap.put("name", "Shreeya")
                val response = retrofitInstance.getRequest("todos", hashMapOf())
                apiListener?.onSuccessResponse(response)

            }catch (e: Exception) {
                //Show Error Message
                if (!Utility(context.requireContext()).isNetworkConnected())
                    apiListener?.onErrorResponse("Internet is not available")
                else
                    apiListener?.onErrorResponse("Error Occurred: $e.localizedMessage")
            }
        }
    }
}

//Create factory for passing paras in ToDoViewModel
class TodoViewModelFactory(private val ctx: Fragment): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodoViewModel(ctx) as T
    }

}
