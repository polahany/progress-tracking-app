package com.example.progresstrackingapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progresstrackingapp.model.Task

@Composable
fun TaskCard(
    task: Task,
    progress: Int,
    onComplete: () -> Unit,
    onExpand: () -> Unit,
    edit: () -> Unit,
    delete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            MainTaskRow(
                task = task,
                onComplete = onComplete,
                onExpand = onExpand,
                expanded = task.isExpanded,
            )
            if (task.isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                ExpandedTaskPart(
                    task = task,
                    edit = edit,
                    delete = delete,
                    progress = progress
                )
            }
        }
    }
}

@Composable
fun MainTaskRow(
    task: Task,
    expanded: Boolean,
    onComplete: () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onComplete() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )
        }

        IconButton(onClick = onExpand) {
            Icon(
                imageVector = if (!expanded) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                contentDescription = "Expand task"
            )
        }
    }
}

@Composable
fun ExpandedTaskPart(
    progress: Int,
    task: Task,
    edit: () -> Unit,
    delete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = task.task,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ActionRow(
            task = task,
            edit = edit,
            delete = delete,
            progress = progress,
        )
    }
}

@Composable
fun ActionRow(
    task: Task,
    progress: Int,
    edit: () -> Unit,
    delete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            ProgressBar(
                task = task,
                progress = progress
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Streak: ${task.streakDays} days",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp
            )
        }

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = edit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
            IconButton(onClick = delete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFB00020)
                )
            }
        }
    }
}
