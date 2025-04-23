package ie.setu.vendorinventorymanagement.ui.screens.productmanagement

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Print
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import ie.setu.vendorinventorymanagement.navigation.Home
import ie.setu.vendorinventorymanagement.ui.components.general.BottomAppBarProvider
import ie.setu.vendorinventorymanagement.ui.components.general.TopAppBarProvider
import ie.setu.vendorinventorymanagement.navigation.allDestinations
import ie.setu.vendorinventorymanagement.ui.components.general.MenuItem
import ie.setu.vendorinventorymanagement.navigation.listOfHomeTiles
import ie.setu.vendorinventorymanagement.ui.screens.productmanagement.ProductManagementViewModel
import ie.setu.vendorinventorymanagement.data.models.ProductsModel
import ie.setu.vendorinventorymanagement.ui.components.general.Centre
import ie.setu.vendorinventorymanagement.ui.components.general.ShowLoader
import ie.setu.vendorinventorymanagement.ui.components.products.ProductText
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.firebase.services.Product

@Composable
fun ProductManagementScreen(modifier: Modifier = Modifier,navController: NavHostController, productviewModel: ProductManagementViewModel = hiltViewModel()){
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(MenuItem.StockTracking) }
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = listOfHomeTiles.find { it.route == currentDestination?.route }?:Home
    val products = productviewModel.uiProducts.collectAsState().value
    val isError = productviewModel.iserror.value
    val error = productviewModel.error.value
    val isLoading = productviewModel.isloading.value
    VendorInventoryManagementTheme {
        Scaffold(

            topBar = {
                TopAppBarProvider(
                    currentScreen = currentTileScreen,
                    canNavigateBack = navController.previousBackStackEntry != null
                ) {
                    navController.navigateUp()
                }
            },
            bottomBar = {
                BottomAppBarProvider(navController,
                    currentScreen = currentBottomScreen,)
            },


        )
        { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ){
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ){
                    //Text("Welcome to Product Management Page!", fontSize = 24.sp)
                    if(isLoading) ShowLoader("Loading Products...")
                    //ProductText()
                    if(products.isEmpty() && !isError)
                        Centre(Modifier.fillMaxSize()) {
                            Text(
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                lineHeight = 34.sp,
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.empty_list)
                            )


                        }
                    if(!isError){
                        LazyColumn(modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)) {
                            items(products){ product ->
                                ProductCardList(product)
                            }
                        }
                    }
                }
                Button(
                    onClick = { navController.navigate("add_product") {
                        popUpTo("add_product") { inclusive = true }
                    }},
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 36.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add Product")
                }
            }

        }

    }

}

@SuppressLint("ResourceAsColor")
@Composable
fun ProductCardList(product: Product){
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(text = product.productCategory, style = MaterialTheme.typography.titleMedium)
                if(product.totalQuantity>0){
                    Text(
                        text = stringResource(R.string.in_stock),
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(color = R.color.green), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }else{
                    Text(
                        text = stringResource(R.string.out_of_stock),
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(R.color.red), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }
            }
            Text("Stock: ${product.totalQuantity}", fontSize = 14.sp)
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text("Brand:${product.brandName}")
                    Text("Vendor:${product.vendorName}")
                    Text("Location:${product.location}")
                    Text("Price:${product.price}")
                }
                if(product.individualQuantities.isNotEmpty()){
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Brand-Wise BreakDown:")
                    product.individualQuantities.forEach{(brand, quantity) ->
                        Text("$brand:$quantity units")
                    }
                }
                val icon = when (product.productCategory.lowercase()){
                    "laptops" -> Icons.Default.Laptop
                    "phones" -> Icons.Default.PhoneAndroid
                    "printers" -> Icons.Default.Print
                    else -> Icons.Default.Inventory2
                }
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = "Category Icon",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}