package com.marco.pocexperiencebar.features.home

sealed class HomeViewAction {
    object Set {
        data class SetAnimationExperience(val experience: Float) : HomeViewAction()
    }
}