package com.marco.pocexperiencebar.core.uikit.components

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marco.pocexperiencebar.core.uikit.utils.toDp


@Composable
fun ExperienceCircle(
    circleSize: Dp,
    initialValue: Float,
    primaryColor: Color,
    secondaryColor: Color,
    textColor: Color,
    insideCircleColor: Color = Color.White,
    maxValue: Float = 0f,
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    Box(
        modifier = Modifier
            .size(circleSize)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            val textSizeCounter = width / 3.5f
            val strokeSize = width * 0.10f
            val circleRadius = width / 2 - strokeSize
            val strokeRadius = circleRadius + strokeSize / 2

            circleCenter = Offset(x = width / 2f, y = height / 2f)

            drawCircle(
                color = insideCircleColor,
                radius = circleRadius,
                center = circleCenter
            )
            drawCircle(
                style = Stroke(
                    width = strokeSize
                ),
                color = secondaryColor,
                radius = strokeRadius,
                center = circleCenter
            )
            val sweepAngle: Float = if (maxValue == 0f) {
                0f
            } else {
                (360f / maxValue) * initialValue
            }
            drawArc(
                color = primaryColor,
                startAngle = 90f,
                sweepAngle = sweepAngle,
                style = Stroke(
                    width = strokeSize,
                    cap = StrokeCap.Round
                ),
                useCenter = false,
                size = Size(
                    width = strokeRadius * 2f,
                    height = strokeRadius * 2f
                ),
                topLeft = Offset(
                    (width - strokeRadius * 2f) / 2f,
                    (height - strokeRadius * 2f) / 2f
                )

            )
            drawContext.canvas.nativeCanvas.apply {
                drawIntoCanvas {
                    drawText(
                        "${initialValue.toInt()}",
                        circleCenter.x,
                        circleCenter.y + (textSizeCounter / 3),
                        Paint().apply {
                            textSize = textSizeCounter
                            textAlign = Paint.Align.CENTER
                            color = textColor.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ExperienceBar(
    maxExperience: Float,
    currentExperience: Float,
) {
    val progress = currentExperience / maxExperience

    LinearProgressIndicator(
        modifier =
        Modifier
            .padding(top = 16.dp, bottom = 4.dp)
            .fillMaxWidth(0.8f)
            .height(10.dp)
            .clip(RoundedCornerShape(16.dp)),
        progress = progress,
        color = Color.Yellow
    )
    Text(
        text = "$currentExperience/$maxExperience",
        modifier =
        Modifier
            .fillMaxWidth(0.8f),
        color = Color.Blue,
        textAlign = TextAlign.End
    )
}


@Composable
fun QuizProgressBar(
    modifier: Modifier = Modifier,
    maxValue: Float,
    currentValue: Float,
    progressColor: Color = Color(0xFFFFD700), // Gold
) {
    // Avoid division by zero and ensure progress is valid
    val safeMaxValue = if (maxValue > 0) maxValue else 1f
    val safeCurrentValue = if (currentValue.isNaN() || currentValue < 0) 0f else currentValue
    val progress by animateFloatAsState(
        targetValue = (safeCurrentValue / safeMaxValue),
        animationSpec = tween(durationMillis = 2000), // Slowing down the animation
        label = ""
    )

    val textWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val paddingHorizontalColumn = 24.dp
    val paddingHorizontalTotal = 48.dp
    val paddingLinearProgressIndicator = 12.dp
    val paddingLinearProgressIndicatorTotal = 24.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = paddingHorizontalColumn)
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .padding(horizontal = paddingLinearProgressIndicator)
                .height(10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            progress = progress,
            color = progressColor,
            trackColor = Color.Gray.copy(alpha = 0.3f)
        )

        val screenWidth = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
        val halfTextWidth = textWidth / 2
        val maxOffset =
            screenWidth.toDp() - paddingHorizontalTotal - paddingLinearProgressIndicatorTotal - halfTextWidth

        IconText(
            maxOffset = maxOffset,
            progress = progress,
            density = density,
            textWidth = textWidth,
            paddingLinearProgressIndicator = paddingLinearProgressIndicator
        )
    }
}

@Composable
fun IconText(
    maxOffset: Dp,
    progress: Float,
    density: Density,
    textWidth: Dp,
    paddingLinearProgressIndicator: Dp,
) {
    var textWidthState by remember { mutableStateOf(textWidth) }

    Column(
        modifier = Modifier
            .offset(
                x = (maxOffset * progress) + paddingLinearProgressIndicator - textWidthState.div(
                    2
                )
            )
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Canvas(
            modifier = Modifier
                .size(12.dp)
                .background(Color.Transparent)
        ) {
            val path = Path().apply {
                moveTo(size.width / 2, 0f) // Start at the top
                lineTo(0f, size.height) // Bottom-left vertex
                lineTo(size.width, size.height) // Bottom-right vertex
                close()
            }
            drawPath(path, color = Color.Red)
        }

        Text(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    textWidthState = with(density) { coordinates.size.width.toDp() }
                },
            text = "${(progress * 100).toInt()}%",
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExperienceCircle(
            circleSize = 100.dp,
            initialValue = 0f,
            primaryColor = Color.Green,
            secondaryColor = Color.Yellow,
            textColor = Color.Black,
            insideCircleColor = Color.Blue,
            maxValue = 100f
        )
        Spacer(modifier = Modifier.height(18.dp))
        ExperienceBar(maxExperience = 2000f, currentExperience = 1500f)
    }

}