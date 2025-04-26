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
import ie.setu.vendorinventorymanagement.ui.screens.productmanagement.ProductManagementViewModel
import ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement.PurchaseOrderManagementViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceOrderScreen(modifier: Modifier = Modifier,
                     navController: NavHostController,
                     productId:String,
                     viewModel: ProductManagementViewModel = hiltViewModel(),
                     purchaseOrderViewModel:PurchaseOrderManagementViewModel = hiltViewModel())
{
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = linkingScreens.find { it.route == currentDestination?.route }?:Home
    val context = LocalContext.current
    val isLoading = viewModel.isloading.value
    var updateSuccessDialog by remember { mutableStateOf(false) }
    val expectedDate = remember {
        LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd MM yyyy"))
    }
    val orderPlacedDate = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("dd MM yyyy"))
    }
    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }
    var orderQuantity by remember { mutableIntStateOf(1) }
    var addPurchaseOrderSuccess by remember { mutableStateOf(false) }
    val purchaseProductState by viewModel.editProducts.collectAsState()
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
            ) {
                    purchaseProductState?.let { product ->
                        val productName by remember { mutableStateOf(product.productName) }
                        val brandName by remember { mutableStateOf(product.brandName) }
                        val price by remember { mutableDoubleStateOf(product.price) }
                        val vendorName by remember { mutableStateOf(product.vendorName) }
                        var location by remember { mutableStateOf(product.location) }
                        var individualQuantities by remember { mutableIntStateOf(
                            product.individualQuantities.get(brandName) ?:1) }
                        val quantity by remember { mutableIntStateOf(product.totalQuantity) }
                        var destination by remember { mutableStateOf("") }
                        var buyerEmail by remember { mutableStateOf("") }
                        var buyerPhoneNumber by remember { mutableStateOf("") }
                        val totalCost = orderQuantity * price

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
                                readOnly = true
                            )
                            OutlinedTextField(
                                value = brandName,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.brand_name))},
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false
                            )
                            OutlinedTextField(
                                value = vendorName,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.vendor_name))},
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false
                            )
                            OutlinedTextField(
                                value = location,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.location))},
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false
                            )
                            Text("Available Units: $quantity")
                            Text("Price per Unit: $price")
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = stringResource(R.string.units_req), modifier = Modifier.weight(1f))
                                AmountPicker(value = orderQuantity,maximumValue=quantity, onValueChange = {orderQuantity = it })
                            }
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
                                value = destination,
                                onValueChange = {destination = it},
                                label = { Text(text = stringResource(R.string.destination))},
                                modifier = Modifier.fillMaxWidth(),
                            )
                            OutlinedTextField(
                                value = expectedDate,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.eta))},
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    val updatedProductStockTotal = product.copy(
                                        totalQuantity = quantity - orderQuantity,
                                    )
                                    val purchasedOrder = PurchaseOrder(
                                        orderId = UUID.randomUUID().toString(),
                                        productId = product.id,
                                        productName = productName,
                                        brandName = brandName,
                                        vendorName = vendorName,
                                        location = location,
                                        buyerEmail = buyerEmail,
                                        buyerPhoneNumber = buyerPhoneNumber,
                                        orderedQuantity = orderQuantity,
                                        orderPlacedDate = orderPlacedDate,
                                        expectedDeliveryDate = expectedDate,
                                        destination = destination,
                                        totalCost = totalCost
                                    )
                                    purchaseOrderViewModel.addPurchaseOrder(purchasedOrder, updatedProductStockTotal){
                                        addPurchaseOrderSuccess = true
                                    }
                                }
                            ) {
                                Text(text = stringResource(R.string.place_order))
                            }
                        }
                        if(addPurchaseOrderSuccess){
                            AlertDialog(
                                onDismissRequest = {addPurchaseOrderSuccess = false},
                                title = { Text(text = stringResource(R.string.order_placed))},
                                text = { Text(text = stringResource(R.string.order_placed_success))},
                                confirmButton = {
                                    TextButton(onClick = {
                                        addPurchaseOrderSuccess = false
                                        navController.navigate("purchased_orders")
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