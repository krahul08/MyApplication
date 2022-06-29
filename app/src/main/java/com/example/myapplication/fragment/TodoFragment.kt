package com.example.myapplication.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.appcomponents.network.interfaces.APIListener
import com.example.myapplication.adapter.ToDoAdapter
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.model.Todos
import com.example.myapplication.viewmodel.TodoViewModel
import com.example.myapplication.viewmodel.TodoViewModelFactory
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TodoFragment : Fragment(), APIListener {

    private lateinit var _binding: FragmentFirstBinding
    lateinit var todoViewModel: TodoViewModel

    private val binding get() = _binding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        todoViewModel = ViewModelProvider(this, TodoViewModelFactory(this)).get(TodoViewModel::class.java)
        todoViewModel.apiListener = this

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun callNetworkAPI(todoViewModel: TodoViewModel) {
        // Call api to create new ToDoPost
        todoViewModel.getTodosFromServer()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        // Call network API
        callNetworkAPI(todoViewModel)
    }

    override fun onStarted() {
        //Show progressbar when API is Fetching the response
        _binding.progressBar.visibility = View.VISIBLE
    }

    override fun onSuccessResponse(responseBody: ResponseBody) {
        //Hide progressbar when api response is fetched
        _binding.progressBar.visibility = View.GONE

        //Convert ResponseBody to corresponding POJO class
        val todos: Todos = GsonBuilder().create().fromJson(responseBody.string(), Todos::class.java)
        _binding.rvTodo.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = ToDoAdapter(todos)
        }

    }

    override fun onErrorResponse(errorMessage: String) {
        //hide progressbar
        _binding.progressBar.visibility = View.GONE
        //Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        Log.e("TAG", "onErrorResponse: $errorMessage" )
    }
}