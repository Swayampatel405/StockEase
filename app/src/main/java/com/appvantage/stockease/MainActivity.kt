package com.appvantage.stockease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appvantage.stockease.authentication.AuthenticationScreen
import com.appvantage.stockease.dashboard.DashboardScreen
import com.appvantage.stockease.ui.theme.StockEaseTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            StockEaseTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "authenticate",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("authenticate"){
                            AuthenticationScreen(navController = navController)
                        }
                        composable("dashboard"){
                            DashboardScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

