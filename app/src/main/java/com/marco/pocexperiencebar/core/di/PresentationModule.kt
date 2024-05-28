package com.marco.pocexperiencebar.core.di

import com.marco.pocexperiencebar.features.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { HomeViewModel() }
}