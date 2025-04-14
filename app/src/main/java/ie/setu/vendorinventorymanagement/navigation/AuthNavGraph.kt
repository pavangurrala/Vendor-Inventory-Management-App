package ie.setu.vendorinventorymanagement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ie.setu.vendorinventorymanagement.ui.screens.login.LoginScreen
import ie.setu.vendorinventorymanagement.ui.screens.signup.SignupScreen
import ie.setu.vendorinventorymanagement.ui.screens.home.HomeScreen
@Composable
fun AuthNavgraph(startDestination: String = "login"){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination){
        composable("login"){ LoginScreen(navController)}
        composable("signup") { SignupScreen(navController) }
        composable("home") { HomeScreen(navController)  }
    }
}