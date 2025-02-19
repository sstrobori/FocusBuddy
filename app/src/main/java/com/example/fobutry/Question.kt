package com.example.fobutry


data class Question(
    val questionText: String,
    val options: List<String>, // Expected to have exactly 4 options.
    val correctOption: Int     // 1-based index (e.g., 1 means first option is correct)
)
