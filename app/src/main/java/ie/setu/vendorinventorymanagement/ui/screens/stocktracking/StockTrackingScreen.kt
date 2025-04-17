package ie.setu.vendorinventorymanagement.ui.screens.stocktracking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import ie.setu.vendorinventorymanagement.navigation.StockTracking
import ie.setu.vendorinventorymanagement.ui.components.general.BottomAppBarProvider
import ie.setu.vendorinventorymanagement.navigation.allDestinations
import ie.setu.vendorinventorymanagement.navigation.ProductManagement
import ie.setu.vendorinventorymanagement.ui.components.general.TopAppBarProvider
import ie.setu.vendorinventorymanagement.ui.components.general.MenuItem
@Composable
fun StockTrackingScreen(modifier: Modifier = Modifier,navController: NavHostController){
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(MenuItem.ProductManagement) }
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: StockTracking
    VendorInventoryManagementTheme {
        Scaffold(
            topBar = {
                TopAppBarProvider(
                    currentScreen = currentBottomScreen,
                    canNavigateBack = navController.previousBackStackEntry != null
                ) { navController.navigateUp() }
            },
            bottomBar = {
                BottomAppBarProvider(navController,
                    currentScreen = currentBottomScreen,)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                Text("Welcome to Stock Tracking Page!", fontSize = 24.sp)

            }
        }
    }

}