package com.example.progresstrackingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.progresstrackingapp.model.Task
import com.example.progresstrackingapp.state.ProgressTrackingState
import com.example.progresstrackingapp.state.emptyTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProgressAppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProgressTrackingState())
    val uiState: StateFlow<ProgressTrackingState> = _uiState.asStateFlow()

    var noTitle by mutableStateOf(false)
        private set
    var noDescription by mutableStateOf(false)
        private set
    var taskInPopUp by mutableStateOf(emptyTask)
        private set
    var title by mutableStateOf(taskInPopUp.title)
        private set
    var description by mutableStateOf(taskInPopUp.task)
        private set
    var streak by mutableStateOf(taskInPopUp.streakDays)
        private set
    var progress by mutableStateOf(taskInPopUp.progress)
        private set

    private fun reset() {
        taskInPopUp = emptyTask
        title = emptyTask.title
        description = emptyTask.task
        streak = emptyTask.streakDays
        progress = emptyTask.progress
        noTitle = false
        noDescription = false
    }

    fun openPopUp(task: Task) {
        taskInPopUp = task.copy()
        title = taskInPopUp.title
        description = taskInPopUp.task
        streak = taskInPopUp.streakDays
        progress = taskInPopUp.progress
        _uiState.update { current ->
            current.copy(
                popUp = true,
                taskInPopUp = task.copy()
            )
        }
    }

    fun closePopUp() {
        _uiState.update { current ->
            current.copy(popUp = false)
        }
        reset()
    }

    fun editTitle(newTitle: String) {
        noTitle = false
        title = newTitle
        taskInPopUp = taskInPopUp.copy(title = newTitle)
    }

    fun editDescription(newDescription: String) {
        noDescription = false
        description = newDescription
        taskInPopUp = taskInPopUp.copy(task = newDescription)
    }

    fun progressInc() {
        if (progress < 100) {
            progress = (progress + 10).coerceAtMost(100)
            taskInPopUp = taskInPopUp.copy(progress = progress)
        }
    }


    fun progressDec() {
        if (progress > 0) {
            progress -= 1
            taskInPopUp = taskInPopUp.copy(progress = taskInPopUp.progress.dec())
        }
    }

    fun streakInc() {
        streak += 1
        taskInPopUp = taskInPopUp.copy(streakDays = taskInPopUp.streakDays.inc())
    }

    fun streakDec() {
        if (streak > 0) {
            streak -= 1
            taskInPopUp = taskInPopUp.copy(streakDays = taskInPopUp.streakDays.dec())
        }
    }

    fun addTask() {
        if (title.isEmpty()) noTitle = true
        if (description.isEmpty()) noDescription = true
        if (!noTitle && !noDescription) {
            _uiState.update { current ->
                current.copy(
                    notCompletedTasks = current.notCompletedTasks + taskInPopUp.copy(),
                    popUp = false
                )
            }
            reset()
        }
        Log.d("msg", "addTask: ${taskInPopUp.title}")
    }

    fun deleteTask(taskTitle: String) {
        _uiState.update { current ->
            current.copy(
                notCompletedTasks = current.notCompletedTasks.filterNot { it.title == taskTitle },
                completedTasks = current.completedTasks.filterNot { it.title == taskTitle }
            )
        }
    }

    fun expandTask(taskTitle: String) {
        _uiState.update { current ->
            val updatedNot = current.notCompletedTasks.map {
                if (it.title == taskTitle) it.copy(isExpanded = !it.isExpanded) else it
            }
            val updatedDone = current.completedTasks.map {
                if (it.title == taskTitle) it.copy(isExpanded = !it.isExpanded) else it
            }
            current.copy(
                notCompletedTasks = updatedNot,
                completedTasks = updatedDone
            )
        }
    }

    fun complete(taskTitle: String) {
        _uiState.update { current ->
            val inNot = current.notCompletedTasks.find { it.title == taskTitle }
            val inDone = current.completedTasks.find { it.title == taskTitle }
            when {
                inNot != null -> {
                    val moved = inNot.copy(completed = true)
                    current.copy(
                        notCompletedTasks = current.notCompletedTasks.filterNot { it.title == taskTitle },
                        completedTasks = current.completedTasks + moved
                    )
                }
                inDone != null -> {
                    val moved = inDone.copy(completed = false)
                    current.copy(
                        completedTasks = current.completedTasks.filterNot { it.title == taskTitle },
                        notCompletedTasks = current.notCompletedTasks + moved
                    )
                }
                else -> current
            }
        }
    }

    fun openPopUpEdit(taskTitle: String) {
        taskInPopUp = _uiState.value.notCompletedTasks.find { it.title == taskTitle }
            ?: _uiState.value.completedTasks.find { it.title == taskTitle }!!
        openPopUp(taskInPopUp)
    }

    fun saveEdit(taskTitle: String) {
        Log.d("msg", "saveEdit: $taskTitle")
        val edited = taskInPopUp
        _uiState.update { current ->
            val updatedNot = current.notCompletedTasks.map {
                if (it.title == taskTitle) edited else it
            }
            val updatedDone = current.completedTasks.map {
                if (it.title == taskTitle) edited else it
            }
            current.copy(
                notCompletedTasks = updatedNot,
                completedTasks = updatedDone,
                popUp = false
            )
        }
        reset()
    }
}
