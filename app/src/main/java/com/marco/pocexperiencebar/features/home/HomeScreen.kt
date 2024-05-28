package com.marco.pocexperiencebar.features.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marco.pocexperiencebar.core.uikit.components.ExperienceBar
import com.marco.pocexperiencebar.core.uikit.components.ExperienceCircle
import com.marco.pocexperiencebar.domain.model.Progression
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = koinViewModel()
    HomeScreenFactory(viewModel)
//    AnimatedCounter()
}

@Composable
fun HomeScreenFactory(viewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val action: (HomeViewAction) -> Unit = { viewModel.dispatchViewAction(it) }
        val viewState by viewModel.state.collectAsState()

        val progression = viewState.progression

        ExperienceCircle(
            circleSize = 100.dp,
            initialValue = viewState.circleExperience,
            primaryColor = Color.Green,
            secondaryColor = Color.Yellow,
            textColor = Color.Black,
            insideCircleColor = Color.Blue,
            maxValue = viewState.experience
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Level ${progression?.level}")
        Text(text = progression?.value ?: "")
        ExperienceBar(
            maxExperience = progression?.cap ?: 0f,
            currentExperience = viewState.textExperience
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            action(
                HomeViewAction.Set.SetAnimationExperience(
                    experience = 1500f
                )
            )
        }) {
            Text(text = "Clique to start animation")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreenFactory(HomeViewModel())
}

@Composable
fun AnimatedCounter() {
    // Create a state to hold the current number
    var number by remember { mutableFloatStateOf(0f) }

    // Launch an effect to update the number over time
    LaunchedEffect(Unit) {
        val totalTime = 5000L // Total duration of 5 seconds
        val updateTime = 50L // Update every 50 milliseconds

        for (i in 0..(totalTime / updateTime)) {
            number = (i / (totalTime / updateTime).toFloat()) * 100
            delay(updateTime)
        }
    }

    // Animate the number change smoothly
    val animatedNumber by animateFloatAsState(
        targetValue = number,
        animationSpec = tween(
            durationMillis = 5000,
            easing = LinearEasing
        ), label = ""
    )

    // Convert the animated number to string when it changes
    val animatedNumberText by remember {
        derivedStateOf {
            animatedNumber.toInt().toString()
        }
    }

    // Display the animated number
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = animatedNumberText
        )
    }
}