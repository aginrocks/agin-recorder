package rocks.agin.recorder.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rocks.agin.recorder.ui.components.ClapboardValue
import rocks.agin.recorder.ui.components.NumberInputDialog
import rocks.agin.recorder.util.rememberHapticFeedbackHelper
import kotlin.math.cos
import kotlin.math.sin

enum class EditingValue { NONE, ROLL, SCENE, TAKE }

@Composable
fun ClapboardScreen() {
    var roll by remember { mutableIntStateOf(1) }
    var scene by remember { mutableIntStateOf(1) }
    var take by remember { mutableIntStateOf(1) }
    var editing by remember { mutableStateOf(EditingValue.NONE) }

    val haptics = rememberHapticFeedbackHelper()

    if (editing != EditingValue.NONE) {
        val title = when (editing) {
            EditingValue.ROLL -> "Set Roll"
            EditingValue.SCENE -> "Set Scene"
            EditingValue.TAKE -> "Set Take"
            else -> ""
        }
        val initialValue = when (editing) {
            EditingValue.ROLL -> roll
            EditingValue.SCENE -> scene
            EditingValue.TAKE -> take
            else -> 0
        }

        NumberInputDialog(
            title = title,
            initialValue = initialValue,
            onConfirm = { newValue ->
                when (editing) {
                    EditingValue.ROLL -> roll = newValue
                    EditingValue.SCENE -> scene = newValue
                    EditingValue.TAKE -> take = newValue
                    else -> {}
                }
                editing = EditingValue.NONE
            },
            onDismiss = { editing = EditingValue.NONE }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Expressive background shapes
            ExpressiveBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "RECORDER",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                ClapboardValue(
                    label = "Roll",
                    value = roll.toString().padStart(3, '0'),
                    onClick = {
                        roll++
                        haptics.heavyClick()
                    },
                    onLongClick = { editing = EditingValue.ROLL },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ClapboardValue(
                        label = "Scene",
                        value = scene.toString(),
                        onClick = {
                            scene++
                            haptics.heavyClick()
                        },
                        onLongClick = { editing = EditingValue.SCENE },
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    ClapboardValue(
                        label = "Take",
                        value = take.toString(),
                        onClick = {
                            take++
                            haptics.heavyClick()
                        },
                        onLongClick = { editing = EditingValue.TAKE },
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun ExpressiveBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val animFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val color1 = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    val color2 = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f)
    val backgroundColor = MaterialTheme.colorScheme.background

    Canvas(modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.5f)) {
        val width = size.width
        val height = size.height

        // Dynamic "blobs"
        val angle = animFloat * 2 * Math.PI
        val xOffset = sin(angle).toFloat() * 100f
        val yOffset = cos(angle).toFloat() * 100f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color1, backgroundColor),
                center = Offset(width * 0.2f + xOffset, height * 0.2f + yOffset),
                radius = 600f
            ),
            center = Offset(width * 0.2f + xOffset, height * 0.2f + yOffset),
            radius = 600f
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color2, backgroundColor),
                center = Offset(width * 0.8f - xOffset, height * 0.7f - yOffset),
                radius = 800f
            ),
            center = Offset(width * 0.8f - xOffset, height * 0.7f - yOffset),
            radius = 800f
        )
    }
}
