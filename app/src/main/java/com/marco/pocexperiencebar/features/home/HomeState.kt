package com.marco.pocexperiencebar.features.home

import com.marco.pocexperiencebar.domain.model.Progression

data class HomeState(
    var experience: Float = 0f,
    val circleExperience: Float = 0f,
    val barExperience: Float = 500f,
    val textExperience: Float = 500f,
    val progression: Progression? = Progression.NEWBIE,
    val targetWordsNumber: Float = 0f,
    val totalWordsNumber: Float? = null,
)