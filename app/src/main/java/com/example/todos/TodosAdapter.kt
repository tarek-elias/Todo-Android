package com.example.todos

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TodosAdapter(val context: Context, val TodoList: ArrayList<Todos>):
    RecyclerView.Adapter<TodosAdapter.TodoViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.todo_list_item, parent, false)
        return TodoViewHolder(view)


    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = TodoList[position]
        holder.todoText.text = currentTodo.content
        holder.todoCheck.setTag(currentTodo.id)
        if(currentTodo.isDone == true)
        {
            holder.todoCheck.isChecked = true
            holder.todoText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.todoImg.setImageDrawable(context.resources.getDrawable(R.drawable.checked))
            }}
        holder.todoCheck.setOnCheckedChangeListener{view,isChecked ->
            if(currentTodo.isDone == true) {
                holder.todoCheck.isChecked = true
                (context as MainActivity).showConfirmation(currentTodo.id)

            }
            else{
                (context as MainActivity).toggleStatus(currentTodo.id)
            }
        }


    }

    override fun getItemCount(): Int {
        return TodoList.size
    }

    class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val todoText = itemView.findViewById<TextView>(R.id.todo_list_item_content)
        val todoCheck = itemView.findViewById<CheckBox>(R.id.todo_list_item_check)
        val todoImg = itemView.findViewById<ImageView>(R.id.todo_list_item_image)

    }

    }