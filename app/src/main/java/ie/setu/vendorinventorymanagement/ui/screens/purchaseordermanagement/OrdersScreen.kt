package ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
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
import ie.setu.vendorinventorymanagement.data.models.PurchaseOrdersModel
import ie.setu.vendorinventorymanagement.ui.components.general.Centre
import ie.setu.vendorinventorymanagement.ui.components.general.ShowLoader
import ie.setu.vendorinventorymanagement.ui.components.purchase.OrdersText
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.ui.components.purchase.PurchaseText
import ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement.PurchaseOrderManagementViewModel
import ie.setu.vendorinventorymanagement.firebase.services.PurchaseOrder

@Composable
fun OrdersScreen(modifier: Modifier = Modifier,navController: NavHostController, orderViewModel:PurchaseOrderManagementViewModel = hiltViewModel()){
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = listOfHomeTiles.find { it.route == currentDestination?.route }?:Home
    val orders = orderViewModel.uiOrders.collectAsState().value
    val isError = orderViewModel.iserror.value
    val error = orderViewModel.error.value
    val isLoading = orderViewModel.isloading.value

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

            ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                if(isLoading) ShowLoader("Loading Orders...")
                OrdersText()
                if(!isError){
                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp))  {
                        items(orders){order->
                            OrdersCardList(order, orderViewModel, navController)
                        }
                    }
                }
            }

        }

}

@Composable
fun OrdersCardList(order: PurchaseOrder, orderViewModel: PurchaseOrderManagementViewModel, navController: NavHostController){
    val cardHeaderText = order.productName+"-"+order.orderId.takeLast(5)
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val delete_message = stringResource(R.string.order_deletion_success)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp,vertical = 5.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ){
        Column(modifier = Modifier.padding(5.dp)){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = Icons.Default.ShoppingBag
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = "Order Icon",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp),
                        tint = Color.White
                    )
                    Text(text = cardHeaderText, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
                }

            }
            Text("Ordered Quantity: ${order.orderedQuantity}", fontSize = 14.sp, modifier = Modifier.padding(8.dp))
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(12.dp)){
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Order ID")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Product Name")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Total Cost")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Product Location")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Delivery Address")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Order Placed Date")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Expected Delivery")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Payment")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Buyer Phone")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Buyer e-mail")
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(order.orderId.takeLast(5))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.productName)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("$${order.totalCost}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.location)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.destination)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.orderPlacedDate)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.expectedDeliveryDate)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.payment)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.buyerPhoneNumber)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.buyerEmail)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()){
                        IconButton(onClick = {
                            navController.navigate("edit_order/${order.orderId}")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.buy),
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            showDeleteDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.buy),
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            navController.navigate("pay_order/${order.orderId}")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = stringResource(R.string.buy),
                                tint = Color.White
                            )
                        }


                    }
                }
            }
        }
    }
    if(showDeleteDialog){
        AlertDialog(
            onDismissRequest = {showDeleteDialog = false},
            title = { Text(text = stringResource(R.string.confirm_deletion_text))},
            text = { Text(text = stringResource(R.string.confirm_Order_deletion))},
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    orderViewModel.deleteOrder(order)
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