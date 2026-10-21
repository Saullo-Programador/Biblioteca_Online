package com.example.biblioteca.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavItem (
    val label : String,
    val icon : ImageVector,
    val route : String
)

val bottomNavItem = listOf(

    BottomNavItem(
        label = "Home",
        icon = Icons.Filled.Home,
        route = Screen.Home.route
    ),

    BottomNavItem(
        label = "Search",
        icon = Icons.Filled.Search,
        route = Screen.Search.route
    ),

    BottomNavItem(
        label = "Biblioteca",
        icon = Icons.Filled.Menu,
        route = Screen.Library.route
    ),

    BottomNavItem(
        label = "Lendo",
        icon = Icons.Filled.CheckCircle,
        route = Screen.Reading.route
    ),

)