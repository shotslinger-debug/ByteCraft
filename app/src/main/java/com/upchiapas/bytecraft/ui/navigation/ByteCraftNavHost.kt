package com.upchiapas.bytecraft.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upchiapas.bytecraft.ByteCraftApplication
import com.upchiapas.bytecraft.presentation.auth.AuthViewModel
import com.upchiapas.bytecraft.presentation.auth.LoginScreen
import com.upchiapas.bytecraft.presentation.auth.RegisterScreen
import com.upchiapas.bytecraft.presentation.home.HomeScreen
import com.upchiapas.bytecraft.presentation.home.HomeViewModel

@Composable
fun ByteCraftNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val app = context.applicationContext as ByteCraftApplication
    val container = app.container

    val authViewModel: AuthViewModel = viewModel(factory = container.authViewModelFactory)
    val homeViewModel: HomeViewModel = viewModel(factory = container.homeViewModelFactory)

    val startDestination = if (container.sessionManager.isLoggedIn()) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Registro
        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Principal CRUD (Home)
        composable(Screen.Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                onLogout = {
                    homeViewModel.logout()
                    // Cierre de sesión: limpia el backstack impidiendo volver con botón Atrás (RF-04)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
