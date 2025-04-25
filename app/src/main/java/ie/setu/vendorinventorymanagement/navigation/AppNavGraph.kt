package ie.setu.vendorinventorymanagement.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ie.setu.vendorinventorymanagement.ui.screens.login.LoginScreen
import ie.setu.vendorinventorymanagement.ui.screens.signup.SignupScreen
import ie.setu.vendorinventorymanagement.ui.screens.home.HomeScreen
import ie.setu.vendorinventorymanagement.ui.screens.productmanagement.ProductManagementScreen
import ie.setu.vendorinventorymanagement.ui.screens.stocktracking.StockTrackingScreen
import ie.setu.vendorinventorymanagement.ui.screens.profile.ProfileScreen
import ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement.PurchaseOrderManagementScreen
import ie.setu.vendorinventorymanagement.ui.screens.paymentsmanagement.PaymentManagementScreen
import ie.setu.vendorinventorymanagement.ui.screens.productmanagement.AddProductScreen
import ie.setu.vendorinventorymanagement.ui.screens.productmanagement.EditProductScreen

@Composable
fun AppNavgraph(
    modifier: Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues,

){
    NavHost(navController = navController, startDestination = Login.route, modifier = Modifier.padding(paddingValues = paddingValues)){
        composable(route = Login.route){ LoginScreen(navController)}
        composable(route = SignUp.route) { SignupScreen(navController) }
        composable(route = ProductManagement.route) { ProductManagementScreen(modifier = modifier, navController = navController) }
        composable(route = StockTracking.route) { StockTrackingScreen(modifier = modifier, navController = navController) }
        composable(route = PurchaseOrderManagement.route){ PurchaseOrderManagementScreen(modifier = modifier, navController = navController)}
        composable(route = PaymentManagement.route){ PaymentManagementScreen(modifier = modifier, navController = navController)}
        composable(route = Home.route){ HomeScreen(modifier=modifier, navController=navController)}
        composable(route = Profile.route) { ProfileScreen(modifier= modifier, navController = navController)  }
        composable(route = AddProduct.route) { AddProductScreen(modifier= modifier, navController = navController) }
        composable(route = EditProduct.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?:""
            EditProductScreen(modifier= modifier, navController = navController,productId)
        }
    }
}