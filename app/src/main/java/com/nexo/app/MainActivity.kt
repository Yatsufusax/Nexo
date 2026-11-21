package com.nexo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.nexo.app.ui.navigation.NexoRoot
import com.nexo.app.ui.theme.NexoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexoTheme {
                val factory = remember { NexoViewModelFactory((application as NexoApp).container) }
                NexoRoot(factory)
            }
        }
    }
}
