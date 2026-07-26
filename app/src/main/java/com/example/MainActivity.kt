package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.MyDialysisBookTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]

        setContent {
            MyDialysisBookTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}
