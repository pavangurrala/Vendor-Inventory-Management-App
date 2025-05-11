package ie.setu.vendorinventorymanagement.ui.screens.stocktracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import ie.setu.vendorinventorymanagement.navigation.Home
import ie.setu.vendorinventorymanagement.ui.components.general.BottomAppBarProvider
import ie.setu.vendorinventorymanagement.navigation.allDestinations
import ie.setu.vendorinventorymanagement.navigation.ProductManagement
import ie.setu.vendorinventorymanagement.navigation.listOfHomeTiles
import ie.setu.vendorinventorymanagement.ui.components.general.TopAppBarProvider
import ie.setu.vendorinventorymanagement.ui.components.general.MenuItem
import ie.setu.vendorinventorymanagement.ui.screens.productmanagement.ProductManagementViewModel
import ie.setu.vendorinventorymanagement.ui.components.stock.StockTrackerText
@Composable
fun StockTrackingScreen(modifier: Modifier = Modifier,navController: NavHostController, productViewModel: ProductManagementViewModel = hiltViewModel()){
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(MenuItem.ProductManagement) }
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = listOfHomeTiles.find { it.route == currentDestination?.route }?:Home
    val products = productViewModel.uiProducts.collectAsState().value

        Scaffold(
            topBar = {
                TopAppBarProvider(
                    currentScreen = currentTileScreen,
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
                    .padding(16.dp)
                    .fillMaxSize(),
                ){
                StockTrackerText()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                ) {
                    TableHeader("Category", modifier = Modifier.weight(1f))
                    TableHeader("Product Name", modifier = Modifier.weight(1f))
                    TableHeader("Brand", modifier = Modifier.weight(1f))
                    TableHeader("Quantity", modifier = Modifier.weight(1f))
                    TableHeader("Status", modifier = Modifier.weight(1f))

                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(products){product->
                        Row(
                          modifier = Modifier
                              .fillMaxWidth()
                              .padding(vertical = 8.dp)
                        ){
                            TableCell(product.productCategory, modifier = Modifier.weight(1f))
                            TableCell(product.productName, modifier = Modifier.weight(1f))
                            TableCell(product.brandName, modifier = Modifier.weight(1f))
                            TableCell(product.totalQuantity.toString(), modifier = Modifier.weight(1f))
                            val status = if(product.totalQuantity>0) "In Stock" else "Out Of Stock"
                            val statusColor = if(product.totalQuantity>0) Color(0xFF4CAF50) else Color(0xFFF44336)
                            Text(
                                text = status,
                                color = statusColor,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        androidx.compose.material.Divider()
                    }
                }

            }
        }


}
@Composable
fun TableHeader(title:String, modifier: Modifier){
    Text(
        text = title,
        modifier = modifier,
        color = Color.White,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold
    )
}
@Composable
fun TableCell(content:String, modifier: Modifier){
    Text(
        text = content,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,

    )
}