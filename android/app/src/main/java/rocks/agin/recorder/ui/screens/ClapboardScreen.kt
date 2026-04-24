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
import android.content.res.Configuration
import androidx.compose.ui.platform.LocalConfiguration
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "AGIN RECORDER",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                if (isLandscape) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
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
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            valueFontSize = 130.sp
                        )
                        ClapboardValue(
                            label = "Scene",
                            value = scene.toString(),
                            onClick = {
                                scene++
                                take = 1
                                haptics.heavyClick()
                            },
                            onLongClick = { editing = EditingValue.SCENE },
                            modifier = Modifier.weight(1f),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            valueFontSize = 130.sp
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
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            valueFontSize = 130.sp
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
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
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            valueFontSize = 130.sp
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
                                    take = 1
                                    haptics.heavyClick()
                                },
                                onLongClick = { editing = EditingValue.SCENE },
                                modifier = Modifier.weight(1f),
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                valueFontSize = 130.sp
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
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                valueFontSize = 130.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


