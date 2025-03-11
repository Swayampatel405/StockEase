package com.appvantage.stockease.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appvantage.stockease.R
import com.appvantage.stockease.dashboard.BottomNavigationItem.items
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userRole: String, // Role: "Admin", "Manager", "Staff"
    categories: List<String> = listOf("Food", "Clothing", "Electronics", "Cosmetics"),
    onCategoryClick: (String)->Unit,
    navController: NavController
){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(userRole = userRole, navController = navController, onLogout = {}, drawerState = drawerState)
        }
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("profile")}) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ){paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Categories Section
                Text("Categories", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        CategoryCard(category, onCategoryClick)
                    }
                }
            }
        }
    }
}



// Small reusable Composable
@Composable
fun CategoryCard(category: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp)
            .clickable { onClick(category) },
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD1E8E4))
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(category, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    userRole: String,
    onLogout:()->Unit
) {

    val scope = rememberCoroutineScope()
    val items = listOf(
        DrawerItem("Dashboard", icon = painterResource(R.drawable.baseline_home_24), "dashboard"),
        DrawerItem("Stock Management", icon = painterResource(R.drawable.baseline_shopping_cart_24), "stockManagement"),
        DrawerItem("Sales History", icon = painterResource(R.drawable.baseline_history_24), "sales_history"),
        DrawerItem("Profile",icon = painterResource(R.drawable.baseline_person_24), "profile"),
    )

    ModalDrawerSheet(
        modifier = Modifier.width(300.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant) // Fix transparency
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // Add padding
        ) {
            // App Logo & Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_business_24),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "StockEase",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider()
            // Drawer Items
            items.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(item.title, fontSize = 16.sp) },
                    selected = false,
                    icon = { Icon(painter = item.icon, contentDescription = item.title) },
                    modifier = Modifier.padding(vertical = 8.dp),
                    onClick = {

                        Log.d("Navigation", "Navigating to ${item.route}")

                        scope.launch {
                            drawerState.close() // Close drawer on click
                        }
                        if(item.title != "Stock Management"){
                            navController.navigate(item.route)
                        }else{
                            navController.navigate("stockManagement/${URLEncoder.encode("Food", "UTF-8")}")
                        }

                    }
                )
            }

            HorizontalDivider()

            if (userRole == "Admin") {
                NavigationDrawerItem(
                    icon = { painterResource(R.drawable.baseline_people_24)},
                    label = { Text("Manage Users") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close() // Close drawer on click
                        }
                        navController.navigate("manage_users") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true // Prevent duplicate instances
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(
                onClick ={
                    scope.launch {
                        drawerState.close()
                    }
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Icon(painter = painterResource(R.drawable.baseline_exit_to_app_24), contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {

    NavigationBar {
        val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painter = painterResource(item.icon), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


data class DrawerItem(
    val title: String,
    val icon: Painter,
    val route:String
)

