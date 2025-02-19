package com.example.fobutry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class QuizAdapter(
    private val onDeleteClick: (Quiz) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    private val quizList = mutableListOf<Quiz>()
    var onItemClick: ((Quiz) -> Unit)? = null // Callback for opening a quiz

    fun submitList(list: List<Quiz>) {
        quizList.clear()
        quizList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun getItemCount(): Int = quizList.size

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizList[position]
        holder.bind(quiz)

        // Handle quiz item click (opens quiz)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(quiz)
        }

        // Handle delete button click (deletes quiz)
        holder.deleteButton.setOnClickListener {
            onDeleteClick.invoke(quiz)
        }
    }

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quizTitleTextView: TextView = itemView.findViewById(R.id.quiz_title_folder)
        val deleteButton: MaterialButton = itemView.findViewById(R.id.delete_quiz_btn) // Ensure it's in layout

        fun bind(quiz: Quiz) {
            quizTitleTextView.text = quiz.quizTitle
        }
    }
}
