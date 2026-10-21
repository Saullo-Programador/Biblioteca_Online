package com.example.biblioteca.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String){
    @Serializable
    data object Splash : Screen("splash")
    @Serializable
    data object Home : Screen("home")
    @Serializable
    data object Search : Screen("search")
    @Serializable
    data object Details : Screen("details/{bookId}"){
        fun createRoute(bookId: String): String {
            return "details/${android.net.Uri.encode(bookId)}"
        }
    }
    @Serializable
    data object Library : Screen("library")
    @Serializable
    data object Reading : Screen("reading")
}