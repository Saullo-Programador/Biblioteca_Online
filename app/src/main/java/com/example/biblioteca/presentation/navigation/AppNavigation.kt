package com.example.biblioteca.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.biblioteca.presentation.details.BookDetailsScreen
import com.example.biblioteca.presentation.home.HomeScreen
import com.example.biblioteca.presentation.library.LibraryScreen
import com.example.biblioteca.presentation.reading.ReadingScreen
import com.example.biblioteca.presentation.search.SearchScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Library.route,
        Screen.Reading.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes){
                BottomBar(navController)
            }
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable (Screen.Home.route) {
                HomeScreen(
                    onOpenLibrary = {
                        navController.navigate(Screen.Library.route)
                    }
                )
            }

            composable(Screen.Search.route) {
                SearchScreen(
                    onBookClick = { bookId ->
                        navController.navigate(
                            Screen.Details.createRoute(bookId)
                        )
                    }
                )
            }

            composable(Screen.Library.route) {
                LibraryScreen (
                    onBookClick = { bookId ->
                        navController.navigate(
                            Screen.Details.createRoute(bookId)
                        )
                    }
                )
            }

            composable(Screen.Reading.route) {
                ReadingScreen(
                    onBookClick = { bookId ->
                        navController.navigate(
                            Screen.Details.createRoute(bookId)
                        )
                    }
                )
            }

            composable(
                route = Screen.Details.route,
                arguments = listOf(
                    navArgument("bookId") {
                        type = NavType.StringType
                    }
                )
            ) { entry ->
                val bookId = entry.arguments?.getString("bookId")

                if (bookId != null) {
                    BookDetailsScreen(
                        bookId = bookId,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Text("Livro não encontrado.")
                }
            }
        }
    }
}