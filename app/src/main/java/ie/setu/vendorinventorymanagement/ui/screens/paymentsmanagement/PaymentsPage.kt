package ie.setu.vendorinventorymanagement.ui.screens.paymentsmanagement

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.ui.components.payments.PaymentPageText
import ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement.PurchaseOrderManagementViewModel
import ie.setu.vendorinventorymanagement.firebase.services.PurchaseOrder
import ie.setu.vendorinventorymanagement.firebase.services.Payment
import ie.setu.vendorinventorymanagement.navigation.linkingScreens
import ie.setu.vendorinventorymanagement.ui.screens.paymentsmanagement.PaymentManagementViewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun PaymentsPage(modifier: Modifier = Modifier,
                 navController: NavHostController,
                 orderId:String,
                 purchaseOrderViewModel:PurchaseOrderManagementViewModel = hiltViewModel(),
                 paymentsViewModel:PaymentManagementViewModel = hiltViewModel()){
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = linkingScreens.find { it.route == currentDestination?.route }?:Home
    val isError = paymentsViewModel.iserror.value
    val error = paymentsViewModel.error.value
    val isLoading = paymentsViewModel.isloading.value
    val paymentDate = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("dd MM yyyy"))
    }
    LaunchedEffect(orderId) {
        Timber.tag("OrderID").d("OrderId: $orderId")
        purchaseOrderViewModel.getOrderById(orderId)
    }
    var selectedPaymentMethod by remember { mutableStateOf("PayPal") }
    var addPaymentSuccess by remember { mutableStateOf(false) }
    val paymentOrderState by purchaseOrderViewModel.editOrders.collectAsState()
    Timber.tag("OrderPaymentEdit").d("Order for Payment Exists: $paymentOrderState")
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
        ){paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                    PaymentPageText()
                    paymentOrderState?.let { order->
                        val productName by remember { mutableStateOf(order.productName) }
                        val totalCost by remember { mutableDoubleStateOf(order.totalCost) }
                        val orderedQuantity by remember { mutableIntStateOf(order.orderedQuantity) }
                        var buyerEmail by remember { mutableStateOf("") }
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ){
                            OutlinedTextField(
                                value = orderId,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.product_name))},
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )
                            OutlinedTextField(
                                value = productName,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.product_name))},
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )
                            OutlinedTextField(
                                value = "$$totalCost",
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.total_cost))},
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )
                            OutlinedTextField(
                                value = orderedQuantity.toString(),
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.order_quantity))},
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true
                            )
                            OutlinedTextField(
                                value = buyerEmail,
                                onValueChange = {buyerEmail = it},
                                label = { Text(text = stringResource(R.string.product_name))},
                                modifier = Modifier.fillMaxWidth(),

                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = stringResource(R.string.select_payment), style = MaterialTheme.typography.bodyMedium)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPaymentMethod == "PayPal",
                                    onClick = {selectedPaymentMethod = "PayPal"}
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = stringResource(R.string.paypal), style = MaterialTheme.typography.bodyMedium)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPaymentMethod == "Direct",
                                    onClick = {selectedPaymentMethod = "Direct"}
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = stringResource(R.string.direct), style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    val updatedPaymentStatus = order.copy(
                                        payment = "Done"
                                    )
                                    val payment = Payment(
                                        paymentId = UUID.randomUUID().toString(),
                                        orderId = orderId,
                                        productName = productName,
                                        totalCost = totalCost,
                                        orderedQuantity = orderedQuantity,
                                        paymentMethod = selectedPaymentMethod,
                                        paymentStatus = "Paid",
                                        buyerEmail = buyerEmail,
                                        paymentDate = paymentDate,
                                    )
                                    paymentsViewModel.addPayment(payment, updatedPaymentStatus){
                                        addPaymentSuccess = true
                                    }
                                }
                            ) { Text("Pay")}
                        }
                        if(addPaymentSuccess){
                            AlertDialog(
                                onDismissRequest = {addPaymentSuccess = false},
                                title = { Text(text = stringResource(R.string.payment_success))},
                                text = { Text(text = stringResource(R.string.payment_order_success))},
                                confirmButton = {
                                    TextButton(onClick = {
                                        addPaymentSuccess = false
                                        navController.popBackStack()
                                    }) {
                                        Text(text = stringResource(R.string.ok))
                                    }
                                }
                            )
                        }
                    }
            }
        }
    }
}