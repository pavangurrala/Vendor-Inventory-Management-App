package ie.setu.vendorinventorymanagement.ui.components.general
import androidx.compose.runtime.Composable
import ie.setu.vendorinventorymanagement.navigation.bottomAppBarDestinations
import androidx.compose.runtime.*
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import ie.setu.vendorinventorymanagement.navigation.AppDestination
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomAppBarProvider(
    navController: NavHostController,
    currentScreen: AppDestination
){
    var navigationSelectedItem by remember { mutableIntStateOf(0) }
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        bottomAppBarDestinations.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = navigationItem == currentScreen,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.Black
                ),
                label = {
                    Text(text = navigationItem.label)
                },
                icon = {
                    Icon(
                        navigationItem.icon,
                        contentDescription = navigationItem.label
                    )
                },
                onClick = {
                    navigationSelectedItem = index
                    navController.navigate(navigationItem.route){
                        popUpTo(navController.graph.findStartDestination().id){saveState = true}
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomAppBarScreenPreview(){
    VendorInventoryManagementTheme {
        BottomAppBarProvider(
            rememberNavController(),
            bottomAppBarDestinations[1]
        )
    }
}