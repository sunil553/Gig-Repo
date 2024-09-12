package com.llyod.task.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.llyod.task.screens.CountryListScreen

/**
 * define navigation to be routed
 *
 */
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CountryList.route) {
        composable(route = Screen.CountryList.route){
            CountryListScreen()
        }
    }
}

/**
 * define navigation routes
 *
 * @property route
 */
sealed class Screen(val route : String) {
    data object CountryList : Screen("forecast_screen")

}
