package com.example.sports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sports.presentation.add.AddPerformanceScreen
import com.example.sports.presentation.add.AddPerformanceViewModel
import com.example.sports.presentation.list.PerformanceListScreen
import com.example.sports.presentation.list.PerformanceListViewModel
import com.example.sports.ui.theme.SportsTheme
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsTheme {
                SportsApp()
            }
        }
    }
}

@Composable
fun SportsApp() {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
        NavHost(
            navController = navController,
            startDestination = "performance_list",
            modifier = Modifier.padding(top = 0.dp, bottom = 24.dp, start = 0.dp, end = 0.dp)
        ) {
            composable("performance_list") {
                val viewModel: PerformanceListViewModel = koinViewModel()
                PerformanceListScreen(
                    viewModel = viewModel,
                    onNavigateToAdd = { navController.navigate("add_performance") }
                )
            }
            composable("add_performance") {
                val viewModel: AddPerformanceViewModel = koinViewModel()
                AddPerformanceScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SportsAppPreview() {
    SportsTheme {
        SportsApp()
    }
}