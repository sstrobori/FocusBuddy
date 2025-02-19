package com.example.fobutry


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTaskActivity(private val userId: Long, private val onTaskAdded: () -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTodoText = view.findViewById<EditText>(R.id.edit_todo_text)
        val todoSaveButton = view.findViewById<Button>(R.id.save_task_input)

        todoSaveButton.setOnClickListener {
            val todoText = editTodoText.text.toString().trim()

            if (todoText.isEmpty()) {
                Toast.makeText(requireContext(), "Task cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = DatabaseHelper(requireContext())
            val isCompleted = false // Default task state
            val success = dbHelper.addTask(todoText, isCompleted, userId)
            dbHelper.close()

            if (success) {
                Toast.makeText(requireContext(), "Task saved successfully!", Toast.LENGTH_SHORT).show()
                onTaskAdded()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Failed to save task.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
