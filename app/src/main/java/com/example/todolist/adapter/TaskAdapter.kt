package com.example.todo.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.entities.Task
import com.example.todolist.databinding.TaskItemBinding

class TaskAdapter(
    var array: MutableList<Task>,
    var taskInterface: TaskInterface
):RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    class TaskHolder(binding: TaskItemBinding):RecyclerView.ViewHolder(binding.root){
        var title = binding.taskItemTitle
        var text = binding.taskItemText
        var main = binding.taskItemMain
        var photo = binding.taskItemImg

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder(TaskItemBinding.inflate(LayoutInflater.
        from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        var temp = array.get(position)
        holder.title.text = temp.title
        holder.text.text = temp.text
        holder.photo.setImageURI(Uri.parse(temp.filePath))

        holder.main.setOnClickListener {
            taskInterface.onClick(temp)
        }
    }

    interface TaskInterface{
        fun onClick(task:Task)
    }
}