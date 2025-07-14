package com.example.progresstrackingapp.model

data class Task(
    val title: String,
    val task: String,
    val completed: Boolean,
    val progress: Int,
    val streakDays: Int,
    var isExpanded: Boolean,
)
