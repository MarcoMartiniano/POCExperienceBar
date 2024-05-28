package com.marco.pocexperiencebar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marco.pocexperiencebar.features.home.HomeScreen
import com.marco.pocexperiencebar.ui.theme.POCExperienceBarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            POCExperienceBarTheme {
                HomeScreen()
            }
        }
    }
}