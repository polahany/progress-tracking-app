package com.example.progresstrackingapp.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.progresstrackingapp.model.Task

@Composable
fun ProgressBar(
    task: Task,
    progress: Int,
    modifier: Modifier = Modifier
) {
    val targetProgress = remember(progress) {
        progress.coerceIn(0, 100) / 100f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 700,
            delayMillis = 100
        ),
        label = "Progress Animation"
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "$progress%", style = MaterialTheme.typography.labelMedium)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF6200EA)) // purple
                    .animateContentSize()
            )
        }
    }
}
