package com.marco.pocexperiencebar.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marco.pocexperiencebar.domain.model.Progression
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private var _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun dispatchViewAction(viewAction: HomeViewAction) {
        when (viewAction) {
            is HomeViewAction.Set.SetAnimationExperience -> {
                setAnimationExperience(
                    experience = viewAction.experience
                )
            }
        }
    }

    private fun setAnimationExperience(
        experience: Float,
        animationDuration: Long = 1500L,
        updateTime: Long = 30L,
    ) {
        // Set the experience
        _state.update {
            it.copy(
                experience = experience
            )
        }

        viewModelScope.launch {
            // Number of iterations
            val iterations = animationDuration / updateTime // 1500/30 = 50
            // Number of experience per iterations
            val acc = experience / iterations // 1500/50 = 30

            for (i in 1..iterations) {
                // Return launch viewModelScope when reach Progression.MAX)
                if (state.value.progression == Progression.MAX) return@launch

                // Percentage of progress for each iterations
                val progress = i.toFloat() / iterations

                // Value of experience according to the progress percentage
                var circleExperience = progress * experience

                // Calculate barExperience based on the initial value and progress
                val barExperience = _state.value.barExperience + (progress * experience)

                // Sum acc to state.value.textExperience
                var textExperience = state.value.textExperience + acc

                // Delay interval = updateTime
                delay(updateTime)

                // Get current progressCap
                val progressCap = state.value.progression?.cap ?: 0f

                // Check if the progressCap was reached in this iteration
                if (textExperience > progressCap) {
                    // Value that exceeds progressCap
                    val exceed = textExperience.minus(progressCap)

                    // Sum 1 to progression level
                    val nextLevel = state.value.progression?.level?.plus(1) ?: 0

                    // If nextLevel is the last level, we have to not let exceed the experience
                    // If is not last level, we set the value of exceed
                    if (nextLevel == 4) {
                        // Set textExperience the ProgressCap of Progression.MAX
                        textExperience = Progression.MAX.cap

                        // Reduce the exceed experience per iteration from circleExperience
                        circleExperience = circleExperience.minus(exceed)
                    } else {
                        // set textExperience the exceeded value
                        textExperience = exceed
                    }

                    // Update progression
                    _state.update {
                        it.copy(
                            progression = Progression.getProgressByLevel(nextLevel)
                        )
                    }
                }

                // Update circleExperience, barExperience, textExperience
                _state.update {
                    it.copy(
                        circleExperience = circleExperience,
                        barExperience = barExperience,
                        textExperience = textExperience
                    )
                }
            }
        }
    }
}