package com.marco.pocexperiencebar.features.home

sealed class HomeViewAction {
    object Set {
        data class SetAnimationExperience(val experience: Float) : HomeViewAction()
        data class WordNumber(val totalWordsNumber: Float, val targetWordsNumber: Float) :
            HomeViewAction()
    }
}