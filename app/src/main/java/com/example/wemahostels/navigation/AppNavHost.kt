package com.example.wemahostels.navigation

import CustomerDashboard
import UpdateClientScreen
import UpdateHouseScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wemahostels.data.HouseDetailsModel

import com.example.wemahostels.data.HouseViewModel
import com.example.wemahostels.ui.theme.screens.SplashScreen

import com.example.wemahostels.ui.theme.screens.dashboard.AdminDashboard
//import com.example.wemahostels.ui.theme.screens.dashboard.CustomerDashboard
import com.example.wemahostels.ui.theme.screens.house.AddHouseScreen
import com.example.wemahostels.ui.theme.screens.house.FeedBackScreen
import com.example.wemahostels.ui.theme.screens.house.HouseDetailsScreen


import com.example.wemahostels.ui.theme.screens.house.PaymentScreen
import com.example.wemahostels.ui.theme.screens.house.SearchScreen
import com.example.wemahostels.ui.theme.screens.house.ViewHouseScreen

import com.example.wemahostels.ui.theme.screens.login.LoginScreen
import com.example.wemahostels.ui.theme.screens.signup.SignupScreen
import com.example.wemahostels.ui.theme.screens.users.UpdateProfileScreen
import com.example.wemahostels.ui.theme.screens.users.UserProfileScreen
import com.example.wemahostels.ui.theme.screens.users.ViewClients
import com.example.wemahostels.ui.theme.screens.users.ViewHouse
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SPLASH
) {
    // Create HouseViewModel instance
    val houseViewModel: HouseViewModel = viewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_SPLASH) {
            SplashScreen {
                navController.navigate(ROUTE_REGISTER) {
                    popUpTo(ROUTE_SPLASH) { inclusive = true }
                }
            }
        }

        composable(ROUTE_REGISTER) { SignupScreen(navController) }
        composable(ROUTE_LOGIN) { LoginScreen(navController) }
        composable(ROUTE_HOME_ONE) { AdminDashboard(navController) }
        composable(ROUTE_ADD_HOUSE) { AddHouseScreen(navController) }

        composable("$ROUTE_UPDATE_CLIENT/{id}") { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("id") ?: ""
            UpdateClientScreen(navController, clientId)
        }

        composable("$ROUTE_EDIT_HOUSE/{id}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("id") ?: ""
            UpdateHouseScreen(navController, houseId)
        }

        composable(ROUTE_USER_PROFILE) { UserProfileScreen(navController) }
        composable(ROUTE_UPDATE_USER_PROFILE) { UpdateProfileScreen(navController) }
        composable(ROUTE_VIEW_HOUSE_ADMIN) { ViewHouse(navController) }
        composable(ROUTE_VIEW_USERS) { ViewClients(navController) }
        composable(ROUTE_EDIT_USERS) { AdminDashboard(navController) }

        composable("search") {
            SearchScreen(houseViewModel) { selectedHouse ->
                navController.navigate("details/${selectedHouse.id}")
            }
        }

        composable(ROUTE_HOME_TWO) {
            CustomerDashboard(navController = navController, houseViewModel = houseViewModel)
        }

        composable("payment/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            PaymentScreen(navController = navController, houseId = houseId)
        }

        composable("viewhouse/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId")
            if (houseId != null) {
                ViewHouseScreen(navController = navController, houseId = houseId)
            } else {
                Text("Invalid House ID")
            }
        }

        composable("booked/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId")
            if (houseId != null) {
                HouseDetailsScreen(navController = navController, houseId = houseId)
            } else {
                Text("Invalid House ID")
            }
        }

        composable("feedback/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId")
            if (houseId != null) {
                FeedBackScreen(navController = navController, houseId = houseId)
            } else {
                Text("Invalid House ID")
            }
        }
    }
}
