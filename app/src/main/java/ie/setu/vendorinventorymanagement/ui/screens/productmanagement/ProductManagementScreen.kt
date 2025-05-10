package ie.setu.vendorinventorymanagement.ui.screens.productmanagement

import android.annotation.SuppressLint
import android.widget.Space
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAlt
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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


import androidx.compose.material3.DropdownMenuItem

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
    var filterOption by remember { mutableStateOf("All") }
    val filterOptions = listOf("All", "In Stock", "Out of Stock")

    val filteredProducts = when(filterOption){
        "In Stock"-> products.filter{it.totalQuantity>0}
        "Out of Stock"-> products.filter { it.totalQuantity<=0 }
        else->products
    }
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
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ){

                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ){
//                    Text("ADD PRODUCTS", fontSize = 24.sp)
                    ProductText()
                    if(isLoading) ShowLoader("Loading Products...")

                    var expanded by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .border(BorderStroke(1.dp, Color.Gray), shape = MaterialTheme.shapes.small)
                            .padding(8.dp),

                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = "Filter",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box{
                            TextButton(onClick = {expanded = true}) {
                                Text(filterOption)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = {expanded = false},
                                modifier = Modifier.border(BorderStroke(1.dp, Color.LightGray))
                            ){
                                filterOptions.forEach{option->
                                    DropdownMenuItem(
                                        text={Text(option)},
                                        onClick={
                                            filterOption = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    if(filteredProducts.isEmpty()&&!isError){
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
                    }
//                    if(products.isEmpty() && !isError)
//                        Centre(Modifier.fillMaxSize()) {
//                            Text(
//                                color = MaterialTheme.colorScheme.secondary,
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 30.sp,
//                                lineHeight = 34.sp,
//                                textAlign = TextAlign.Center,
//                                text = stringResource(R.string.empty_list)
//                            )
//
//
//                        }
                    if(!isError){
                        LazyColumn(modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)) {
                            items(filteredProducts){ product ->
                                ProductCardList(product, productviewModel, navController)
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ResourceAsColor")
@Composable
fun ProductCardList(product: Product, productviewModel: ProductManagementViewModel,navController: NavHostController){
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val delete_message = stringResource(R.string.product_deletion_success)
    val tooltipState = rememberTooltipState()
    val cardHeaderText = product.productCategory +" - "+product.brandName
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp,vertical = 5.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ){
        Column(modifier = Modifier.padding(5.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp),
                        tint = Color.White
                    )
                    Text(text = cardHeaderText, style = MaterialTheme.typography.titleMedium)
                }

                if(product.totalQuantity>0){
                    Text(
                        text = stringResource(R.string.in_stock),
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(R.color.green), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        fontSize = 12.sp
                    )
                }else{
                    Text(
                        text = stringResource(R.string.out_of_stock),
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(R.color.red), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        fontSize = 12.sp
                    )
                }
            }
            Text("Stock: ${product.totalQuantity}", fontSize = 14.sp, modifier = Modifier.padding(8.dp))
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Brand")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Product Name")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Vendor")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Location")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Price")

                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.brandName)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(product.productName)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(product.vendorName)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(product.location)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("$${product.price}/unit")

                        }
                    }
//                    Text("Brand:${product.brandName}")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Product Name:${product.productName}")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Vendor:${product.vendorName}")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Location:${product.location}")
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Price:$${product.price}/unit")
//                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()){
                        IconButton(onClick = {
                            navController.navigate("edit_product/${product.id}")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_product),
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            showDeleteDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_product),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                    }
                }
                if(product.individualQuantities.isNotEmpty()){
                    Spacer(modifier = Modifier.height(8.dp))
                    //Text("BreakDown:")
                    Spacer(modifier = Modifier.height(8.dp))
//                    product.individualQuantities.forEach{(brand, quantity) ->
//                        Text("$brand:$quantity units")
//                    }
                }



            }
        }
    }
    if(showDeleteDialog){
      AlertDialog(
          onDismissRequest = {showDeleteDialog = false},
          title = { Text(text = stringResource(R.string.confirm_deletion_text))},
          text = { Text(text = stringResource(R.string.confirm_Product_deletion))},
          confirmButton = {
              TextButton(onClick = {
                  showDeleteDialog = false
                  productviewModel.deleteProduct(product)
                  Toast.makeText(
                      context,
                      delete_message,
                      Toast.LENGTH_SHORT

                  ).show()
              }) {
                    Text("Yes")
              }
          },
          dismissButton = {
              TextButton(onClick = {
                  showDeleteDialog = false
              }) {
                  Text("No")
              }
          }
      )
    }
}