package com.example.fobutry

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var taskList: MutableList<Task>,
    private val dbHelper: DatabaseHelper,
    private val userId: Long
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBoxtodo: CheckBox = view.findViewById(R.id.checkbox_todo)
        val taskTitle: TextView = view.findViewById(R.id.task_title)
        val deleteTaskBtn: Button = view.findViewById(R.id.delete_task_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskTitle.text = task.taskTitle
        holder.checkBoxtodo.isChecked = task.isCompleted  // Load saved state

        // Update database when checkbox is checked or unchecked
        holder.checkBoxtodo.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            dbHelper.updateTaskCompletion(task.taskTitle, isChecked, userId)  // Update in database
        }

        holder.deleteTaskBtn.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    val success = dbHelper.deleteTask(task.taskTitle, task.isCompleted, userId)
                    if (success) {
                        taskList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount() = taskList.size

    fun updateTasks(newTasks: List<Task>) {
        taskList.clear()
        taskList.addAll(newTasks)
    }
}
