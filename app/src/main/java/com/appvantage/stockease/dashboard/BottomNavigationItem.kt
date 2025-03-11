package com.appvantage.stockease.dashboard

import androidx.compose.ui.res.painterResource
import com.appvantage.stockease.R

data class BottomNavItem(
    val title: String,
    val icon: Int,
    val route: String
)

object BottomNavigationItem {
    val StockManagement = BottomNavItem("Stock", R.drawable.baseline_shopping_cart_24, "stock_management")
    val SalesHistory = BottomNavItem("Sales", R.drawable.baseline_history_24, "sales_history")
    val Profile = BottomNavItem("Profile", R.drawable.baseline_person_24, "profile")
    val items = listOf(
        StockManagement, SalesHistory, Profile
    )
}