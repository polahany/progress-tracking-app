package com.example.progresstrackingapp.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.progresstrackingapp.components.PopUp
import com.example.progresstrackingapp.components.TaskCard
import com.example.progresstrackingapp.model.Task
import com.example.progresstrackingapp.state.emptyTask
import com.example.progresstrackingapp.ui.theme.ProgressTrackingAppTheme
import com.example.progresstrackingapp.viewmodel.ProgressAppViewModel

@Composable
fun ProgressTrackingAppScreen(
    progressAppViewModel: ProgressAppViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val progressUiState by progressAppViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            ProgressTopAppBar(
                openPopupMenu = { progressAppViewModel.openPopUp(it) },
            )
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            if (progressUiState.popUp) {
                PopUp(
                    reason = progressUiState.state,
                    task = progressAppViewModel.taskInPopUp,
                    title = progressAppViewModel.title,
                    description = progressAppViewModel.description,
                    streak = progressAppViewModel.streak,
                    progress = progressAppViewModel.progress,
                    noTitle = progressAppViewModel.noTitle,
                    noDescription = progressAppViewModel.noDescription,
                    create = { progressAppViewModel.addTask() },
                    edit = { progressAppViewModel.saveEdit(it) },
                    editTitle = { progressAppViewModel.editTitle(it) },
                    editTask = { progressAppViewModel.editDescription(it) },
                    progressInc = { progressAppViewModel.progressInc() },
                    progressDec = { progressAppViewModel.progressDec() },
                    streakInc = { progressAppViewModel.streakInc() },
                    streakDec = { progressAppViewModel.streakDec() },
                    close = { progressAppViewModel.closePopUp() },
                    taskTitle = progressAppViewModel.title
                )
            }

            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                TaskListSection(
                    title = "To Do",
                    tasks = progressUiState.notCompletedTasks,
                    delete = { progressAppViewModel.deleteTask(it) },
                    expand = { progressAppViewModel.expandTask(it) },
                    onComplete = { progressAppViewModel.complete(it) },
                    edit = { progressAppViewModel.openPopUpEdit(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(12.dp))

                TaskListSection(
                    title = "Completed",
                    tasks = progressUiState.completedTasks,
                    delete = { progressAppViewModel.deleteTask(it) },
                    expand = { progressAppViewModel.expandTask(it) },
                    onComplete = { progressAppViewModel.complete(it) },
                    edit = { progressAppViewModel.openPopUpEdit(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskListSection(
    title: String,
    tasks: List<Task>,
    delete: (String) -> Unit,
    expand: (String) -> Unit,
    onComplete: (String) -> Unit,
    edit: (String) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(vertical = 8.dp)
    )
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(tasks, key = { it.title }) {
            TaskCard(
                task = it,
                progress = it.progress,
                onComplete = { onComplete(it.title) },
                onExpand = { expand(it.title) },
                edit = { edit(it.title) },
                delete = { delete(it.title) },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium)
                    .animateItemPlacement()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressTopAppBar(
    openPopupMenu: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Progress Tracker")
        },
        actions = {
            IconButton(
                onClick = { openPopupMenu(emptyTask) }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircleOutline,
                    contentDescription = "add task"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProgressTrackingAppScreenPreview() {
    ProgressTrackingAppTheme {
        ProgressTrackingAppScreen()
    }
}
