package com.upchiapas.bytecraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.upchiapas.bytecraft.presentation.HomeScreen
import com.upchiapas.bytecraft.ui.theme.ByteCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ByteCraftTheme {
                HomeScreen()
            }
        }
    }
}
