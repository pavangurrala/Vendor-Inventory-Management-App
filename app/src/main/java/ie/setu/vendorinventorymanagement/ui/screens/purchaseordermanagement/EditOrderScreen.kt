package ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.setu.vendorinventorymanagement.ui.components.general.BottomAppBarProvider
import ie.setu.vendorinventorymanagement.ui.components.general.TopAppBarProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.hilt.navigation.compose.hiltViewModel
import ie.setu.vendorinventorymanagement.navigation.allDestinations
import ie.setu.vendorinventorymanagement.ui.components.general.MenuItem
import ie.setu.vendorinventorymanagement.navigation.listOfHomeTiles
import ie.setu.vendorinventorymanagement.navigation.linkingScreens
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import ie.setu.vendorinventorymanagement.navigation.Home
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.data.models.BrandNameModel
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.firebase.services.PurchaseOrder
import ie.setu.vendorinventorymanagement.ui.components.general.AmountPicker
import ie.setu.vendorinventorymanagement.ui.components.general.DropDownField
import ie.setu.vendorinventorymanagement.ui.components.general.ShowLoader
import ie.setu.vendorinventorymanagement.ui.screens.productmanagement.ProductManagementViewModel
import ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement.PurchaseOrderManagementViewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOrderScreen(modifier: Modifier = Modifier,
                    navController: NavHostController,
                    orderId:String,
                    productViewModel: ProductManagementViewModel = hiltViewModel(),
                    purchaseOrderViewModel:PurchaseOrderManagementViewModel = hiltViewModel())
{
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = linkingScreens.find { it.route == currentDestination?.route }?:Home
    val context = LocalContext.current
    val isLoading = purchaseOrderViewModel.isloading.value
    var updateSuccessDialog by remember { mutableStateOf(false) }
    val expectedDate = remember {
        LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd MM yyyy"))
    }
    LaunchedEffect(orderId) {
        Timber.tag("OrderID").d("OrderId: $orderId")
        purchaseOrderViewModel.getOrderById(orderId)
    }
    val existingOrdersState by purchaseOrderViewModel.editOrders.collectAsState()
    Timber.tag("OrderEdit").d("Order Exists: $existingOrdersState")
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
                BottomAppBarProvider(
                    navController,
                    currentScreen = currentBottomScreen,
                )
            },
        ){paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                if(isLoading) ShowLoader("Loading Order Details...")
                existingOrdersState?.let {order ->

                    val productName by remember { mutableStateOf(order.productName) }
                    val totalCost by remember { mutableDoubleStateOf(order.totalCost) }
                    val expectedDeliveryDate by remember { mutableStateOf(order.expectedDeliveryDate) }
                    val orderedQuantity by remember { mutableIntStateOf(order.orderedQuantity) }
                    val location by remember { mutableStateOf(order.location) }
                    var destination by remember { mutableStateOf(order.destination) }
                    var buyerEmail by remember { mutableStateOf(order.buyerEmail) }
                    var buyerPhoneNumber by remember { mutableStateOf(order.buyerPhoneNumber) }
                    val orderModifiedDate by remember { mutableStateOf(order.orderModifiedDate) }
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ){
                        OutlinedTextField(
                            value = productName,
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.product_name))},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                        OutlinedTextField(
                            value = orderId,
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.order_id))},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                        OutlinedTextField(
                            value = orderedQuantity.toString(),
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.order_quantity))},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                        OutlinedTextField(
                            value = totalCost.toString(),
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.total_cost))},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                        OutlinedTextField(
                            value = destination,
                            onValueChange = {destination = it},
                            label = { Text(text = stringResource(R.string.order_id))},
                            modifier = Modifier.fillMaxWidth(),
                        )
                        OutlinedTextField(
                            value = buyerEmail,
                            onValueChange = {buyerEmail = it},
                            label = { Text(text = stringResource(R.string.buyer_email))},
                            modifier = Modifier.fillMaxWidth(),
                        )
                        OutlinedTextField(
                            value = buyerPhoneNumber,
                            onValueChange = {buyerPhoneNumber = it},
                            label = { Text(text = stringResource(R.string.buyer_phone_number))},
                            modifier = Modifier.fillMaxWidth(),
                        )
                        OutlinedTextField(
                            value = expectedDeliveryDate,
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.eta))},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                        Button(
                            onClick = {
                                val updatedProduct = order.copy(
                                    orderId = orderId,
                                    productName = productName,
                                    orderedQuantity = orderedQuantity,
                                    totalCost = totalCost,
                                    location = location,
                                    destination = destination,
                                    buyerEmail = buyerEmail,
                                    buyerPhoneNumber = buyerPhoneNumber,
                                    expectedDeliveryDate = expectedDeliveryDate,
                                    orderModifiedDate = orderModifiedDate
                                )
                                purchaseOrderViewModel.updateOrder(updatedProduct){
                                    updateSuccessDialog = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Update Order")
                        }
                    }
                    if (updateSuccessDialog) {
                        AlertDialog(
                            onDismissRequest = { updateSuccessDialog = false },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        updateSuccessDialog = false
                                        navController.popBackStack()
                                    }
                                ) {
                                    Text("Ok")
                                }
                            },
                            title = {
                                Text(text = stringResource(R.string.update_order_success))
                            },
                            text = {
                                Text(text = stringResource(R.string.order_update_success))
                            }
                        )
                    }
                }
            }

        }
    }
}