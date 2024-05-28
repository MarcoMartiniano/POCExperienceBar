package com.marco.pocexperiencebar.core.uikit.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


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