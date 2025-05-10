package ie.setu.vendorinventorymanagement.ui.screens.paymentsmanagement

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.firebase.services.Payment
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import ie.setu.vendorinventorymanagement.navigation.Home
import ie.setu.vendorinventorymanagement.ui.components.general.BottomAppBarProvider
import ie.setu.vendorinventorymanagement.ui.components.general.TopAppBarProvider
import ie.setu.vendorinventorymanagement.navigation.allDestinations
import ie.setu.vendorinventorymanagement.navigation.listOfHomeTiles
import ie.setu.vendorinventorymanagement.ui.components.general.MenuItem
import ie.setu.vendorinventorymanagement.ui.components.general.ShowLoader
import ie.setu.vendorinventorymanagement.ui.components.payments.PaymentsText
import ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement.OrdersCardList

@Composable
fun PaymentManagementScreen(modifier: Modifier = Modifier,navController: NavHostController, paymentManagementViewModel: PaymentManagementViewModel = hiltViewModel()){
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(MenuItem.StockTracking) }
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = listOfHomeTiles.find { it.route == currentDestination?.route }?:Home
    val payments  = paymentManagementViewModel.uiPayments.collectAsState().value
    val isLoading = paymentManagementViewModel.isloading.value
    val isError = paymentManagementViewModel.iserror.value
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
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                if(isLoading) ShowLoader("Loading Payments...")
                PaymentsText()
                if(!isError){
                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp))  {
                        items(payments){payment->
                            PaymentCardList(payment)
                        }
                    }
                }

            }
        }
    }

}
@Composable
fun PaymentCardList(payment: Payment){
    val cardHeaderText =payment.productName +" - "+payment.paymentId.takeLast(5)
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp,vertical = 5.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            val icon = Icons.Default.Paid
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                imageVector = icon,
                contentDescription = "Paid Icon",
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp),
                tint = Color.White
            )
            Text(text = cardHeaderText, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
        }
        Text("Payment Status: ${payment.paymentStatus}", fontSize = 14.sp, modifier = Modifier.padding(10.dp))
        AnimatedVisibility(visible = expanded){
            Column(modifier = Modifier.padding(12.dp)){
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Order ID")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Product Name")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Ordered Quantity")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total Cost")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Payment Method")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Payment Date")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Buyer Email")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(payment.orderId.takeLast(5))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(payment.productName)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(payment.orderedQuantity.toString())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$${payment.totalCost}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(payment.paymentMethod)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(payment.paymentDate)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(payment.buyerEmail)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }
        }
    }

}