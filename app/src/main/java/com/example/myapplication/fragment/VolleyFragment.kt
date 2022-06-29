package com.example.myapplication.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.appcomponents.network.interfaces.VolleyAPIListener
import com.android.appcomponents.util.Utility
import com.example.myapplication.databinding.VolleyFragmentBinding
import com.example.myapplication.model.Todos
import com.example.myapplication.utils.showSnackBar
import com.example.myapplication.viewmodel.VolleyViewModel
import com.google.gson.GsonBuilder

class VolleyFragment : Fragment(), VolleyAPIListener {


    private lateinit var viewModel: VolleyViewModel
    private lateinit var volleyFragmentBinding: VolleyFragmentBinding
    private val _binding get() = volleyFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        volleyFragmentBinding = VolleyFragmentBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(
                this,
                VolleyViewModel.VolleyViewModelFactory(this)
            )[VolleyViewModel::class.java]
        viewModel.apiListener = this


        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun callVolleyAPI(viewModel: VolleyViewModel) {
        //Initialize Utility Class from AppComponent Library
        val utility = Utility(this.requireContext())
        //Check Internet Connectivity
        if (utility.isNetworkConnected()) {
            //Get data from server
            viewModel.getDataFromServer()
        } else {
            //Show message when internet is not available
            showSnackBar(
                this.requireActivity(),
                "Internet is not Available",
                "Enable",
                {
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                })
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        // Call network API
        callVolleyAPI(viewModel)
    }

    override fun onStarted() {
        _binding.progressBar.visibility = View.VISIBLE
    }

    override fun onSuccessResponse(response: String) {
        _binding.progressBar.visibility = View.GONE
        val todos = GsonBuilder().create().fromJson(response, Todos::class.java)
        Log.d("Rahul kumar", todos[0].title)

    }

    override fun onErrorResponse(errorMessage: String) {
        _binding.progressBar.visibility = View.GONE
        makeText(context, errorMessage, LENGTH_SHORT).show()
        Log.e("TAG", "onErrorResponse: $errorMessage")
    }
}