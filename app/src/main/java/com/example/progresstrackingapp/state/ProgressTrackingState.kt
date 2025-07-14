package com.example.progresstrackingapp.state

import com.example.progresstrackingapp.model.Task

val emptyTask = Task(
    title = "",
    task = "",
    completed = false,
    progress = 0,
    streakDays = 0,
    isExpanded = false
)

data class ProgressTrackingState(
    val notCompletedTasks: List<Task> = listOf(),
    val completedTasks: List<Task> = listOf(),
    val popUp: Boolean = false,
    val taskInPopUp: Task = emptyTask,
    val state: String = "add"
)
