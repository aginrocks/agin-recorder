package rocks.agin.recorder.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rocks.agin.recorder.util.rememberHapticFeedbackHelper

@OptIn(ExperimentalAnimationApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ClapboardValue(
    label: String,
    value: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    valueFontSize: androidx.compose.ui.unit.TextUnit = 110.sp
) {
    val haptics = rememberHapticFeedbackHelper()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cornerSize by animateDpAsState(
        targetValue = if (isPressed) 16.dp else 32.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "CornerAnimation"
    )

    Surface(
        modifier = modifier
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptics.click()
                    onClick()
                },
                onLongClick = {
                    haptics.heavyClick()
                    onLongClick()
                }
            ),
        color = containerColor,
        shape = RoundedCornerShape(cornerSize)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = contentColor.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            AnimatedContent(
                targetState = value,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() togetherWith
                            slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                },
                label = "ValueAnimation",
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { targetValue ->
                Text(
                    text = targetValue,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = valueFontSize,
                        fontWeight = FontWeight.Black
                    ),
                    color = contentColor,
                    maxLines = 1,
                    softWrap = false,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
