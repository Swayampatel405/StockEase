package com.appvantage.stockease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appvantage.stockease.authentication.AuthenticationScreen
import com.appvantage.stockease.dashboard.DashboardScreen
import com.appvantage.stockease.dashboard.ProfileScreen
import com.appvantage.stockease.dashboard.SalesHistoryScreen
import com.appvantage.stockease.dashboard.SettingsScreen
import com.appvantage.stockease.dashboard.StockManagementScreen
import com.appvantage.stockease.dashboard.UserManagementScreen
import com.appvantage.stockease.ui.theme.StockEaseTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            StockEaseTheme {
                val navController = rememberNavController()
                val userRoleState = remember { mutableStateOf<String?>(null) }

                // Fetch user role from Firestore
                LaunchedEffect(Unit) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    userId?.let { uid ->
                        val firestore = FirebaseFirestore.getInstance()
                        firestore.collection("users").document(uid)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    userRoleState.value = document.getString("role") ?: "Staff"
                                } else {
                                    userRoleState.value = "Staff" // Default role if no data
                                }
                            }
                            .addOnFailureListener {
                                userRoleState.value = "Staff" // Default role if fetch fails
                            }
                    }
                }
                if (userRoleState.value != null) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            userRole = userRoleState.value ?: "Staff"
                        )
                    }
                } else {
                    // Show a loading screen while fetching role
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier,navController: NavHostController, userRole: String) {
    NavHost(navController = navController, startDestination = "authenticate") {

        composable("authenticate"){
            AuthenticationScreen(navController = navController)
        }

        composable("dashboard") {
            DashboardScreen(
                userRole = userRole,
                onCategoryClick = { category ->
                    navController.navigate("stockManagement/$category")
                },
                navController = navController
            )
        }

        composable("stockManagement/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            StockManagementScreen(navController,category)
        }

        composable("profile") {
            ProfileScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }
        composable("sales_history") {
            SalesHistoryScreen(navController)
        }

        composable("manage_users") {
            if (userRole == "Admin") {
                UserManagementScreen(navController)
            }
        }
    }
}


