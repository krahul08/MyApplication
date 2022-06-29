package com.example.myapplication.adapter

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.RowTodoListItemBinding
import com.example.myapplication.model.Todos
import com.example.myapplication.model.TodosItem

class ToDoAdapter(private val todos: Todos): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTodoListItemBinding.inflate(inflater, parent, false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bindTodo(todos[position])
    }

    override fun getItemCount() = todos.size

    class ToDoViewHolder(private val binding: RowTodoListItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bindTodo(todoItem: TodosItem) {

            with(binding) {
                val captionBuilder = StringBuilder()
                captionBuilder.append(todoItem.title)
                val commentBuilder = SpannableStringBuilder(captionBuilder)
                commentBuilder.setSpan(BulletSpan(15), 0, todoItem.title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvTitle.text = commentBuilder
            }
        }
    }
}