package com.example.progresstrackingapp.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.progresstrackingapp.model.Task

@Composable
fun PopUp(
    reason: String = "edit",
    task: Task,
    title: String,
    description: String,
    streak: Int,
    progress: Int,
    taskTitle: String,
    noTitle: Boolean,
    noDescription: Boolean,
    create: () -> Unit,
    edit: (String) -> Unit,
    editTitle: (String) -> Unit,
    editTask: (String) -> Unit,
    progressInc: () -> Unit,
    progressDec: () -> Unit,
    streakInc: () -> Unit,
    streakDec: () -> Unit,
    close: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier
            .padding(16.dp) ,
        onDismissRequest = close,
        dismissButton = {
            OutlinedButton(onClick = close) {
                Text("Close")
            }
        },
        confirmButton = {
            Button(onClick = {
                if (reason == "add") {
                    create()
                } else {
                    edit(taskTitle)
                }
            }) {
                Text("Done")
            }
        },
        title = {
            Text(text = reason)
        },
        text = {
            EditOptions(
                title = title,
                description = description,
                editTitle = editTitle,
                editTask = editTask,
                task = task,
                progressInc = progressInc,
                progressDec = progressDec,
                streakInc = streakInc,
                streakDec = streakDec,
                streak = streak,
                progress = progress,
                noTitle = noTitle,
                noDescription = noDescription
            )
        }
    )
}

@Composable
fun EditOptions(
    title: String,
    description: String,
    streak: Int,
    progress: Int,
    noTitle: Boolean,
    noDescription: Boolean,
    task: Task,
    editTitle: (String) -> Unit,
    editTask: (String) -> Unit,
    progressInc: () -> Unit,
    progressDec: () -> Unit,
    streakInc: () -> Unit,
    streakDec: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        OutlinedTextField(
            value = title,
            onValueChange = editTitle,
            singleLine = true,
            label = { Text("Title") },
            isError = noTitle
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = editTask,
            singleLine = false,
            label = { Text("Description") },
            isError = noDescription
        )
        ProgressEdit(
            task = task,
            progressInc = progressInc,
            progressDec = progressDec,
            progress = progress
        )
        Spacer(Modifier.height(6.dp))
        StreakEdit(
            task = task,
            streakInc = streakInc,
            streakDec = streakDec,
            streak = streak
        )
    }
}

@Composable
fun ProgressEdit(
    task: Task,
    progressInc: () -> Unit,
    progressDec: () -> Unit,
    progress: Int,
    modifier: Modifier = Modifier
) {
    ProgressBar(task = task, progress = progress)
    Spacer(Modifier.height(6.dp))
    Row (
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Button(onClick = progressDec) {
            Text(text = "Decrement")
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = progressInc) {
            Text(text = "Increment")
        }
    }
}

@Composable
fun StreakEdit(
    task: Task,
    streak: Int,
    streakInc: () -> Unit,
    streakDec: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically ,
    ) {
        Button(onClick = streakDec) {
            Text(text = "dec")
        }
        Spacer(Modifier.width(4.dp))
        Text(
            text = "Streak days: $streak" ,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(4.dp))
        Button(onClick = streakInc) {
            Text(text = "inc")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PopUpPreview() {
    val dummyTask = Task(
        title = "Read a Book",
        task = "Read 20 pages of a self-help book",
        completed = false,
        progress = 3,
        streakDays = 5,
        isExpanded = true
    )

    val title = remember { mutableStateOf("Read a Book") }
    val description = remember { mutableStateOf("Read 20 pages of a self-help book") }

    PopUp(
        reason = "edit",
        task = dummyTask,
        title = title.value,
        description = description.value,
        streak = dummyTask.streakDays,
        progress = dummyTask.progress,
        taskTitle = dummyTask.title,
        noTitle = false,
        noDescription = false,
        create = {},
        edit = {},
        editTitle = { title.value = it },
        editTask = { description.value = it },
        progressInc = {},
        progressDec = {},
        streakInc = {},
        streakDec = {},
        close = {}
    )
}
