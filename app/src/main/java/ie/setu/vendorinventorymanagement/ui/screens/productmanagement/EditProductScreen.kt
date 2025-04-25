package ie.setu.vendorinventorymanagement.ui.screens.productmanagement

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import ie.setu.vendorinventorymanagement.data.models.ProductsModel
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.ui.components.general.AmountPicker
import ie.setu.vendorinventorymanagement.ui.components.general.DropDownField
import ie.setu.vendorinventorymanagement.ui.components.general.ShowLoader
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(modifier: Modifier = Modifier,navController: NavHostController,productId:String,viewModel: ProductManagementViewModel = hiltViewModel()){
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = linkingScreens.find { it.route == currentDestination?.route }?:Home
    val context = LocalContext.current
    val isLoading = viewModel.isloading.value
    var updateSuccessDialog by remember { mutableStateOf(false) }
    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }
    val existingProductState by viewModel.editProducts.collectAsState()
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
        ) {
                paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
//                if(allProducts.isNotEmpty()){
//                    val checkExistingProduct = allProducts.find { it.id == productId }
//                    if(checkExistingProduct!=null){
//                        Timber.tag("ProductEdit").d("Product Exists: $existingProduct")
//                    }else {
//                        Timber.tag("ProductEdit").d("Product with Id $productId does not Exists!")
//                    }
//                    }else{
//                    Timber.tag("ProductEdit").d("Products are loading...")
//                    }
                if(isLoading) ShowLoader("Loading Product Details...")
                existingProductState?.let{product ->
                    var productName by remember { mutableStateOf(product.productName) }
                    var productCategory by remember { mutableStateOf(product.productCategory) }
                    var brandName by remember { mutableStateOf(product.brandName) }
                    var price by remember { mutableStateOf(product.price.toString()) }
                    var vendorName by remember { mutableStateOf(product.vendorName) }
                    var location by remember { mutableStateOf(product.location) }
                    var individualQuantities by remember { mutableIntStateOf(
                        product.individualQuantities.get(brandName) ?:1) }
                    //var quantity by remember { mutableStateOf(product.totalQuantity?:"") }
                    val brands = listOf("Dell", "HP", "Apple", "Samsung", "Lenovo")
                    val categories = listOf("Laptops", "Phones", "Printers")
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        OutlinedTextField(value = productName, onValueChange = {productName = it}, label = {Text(
                            stringResource(R.string.product_name))},modifier = Modifier.fillMaxWidth() )
                        DropDownField("Category",productCategory,categories) {productCategory = it }
                        DropDownField("Brand",brandName, brands ){brandName = it}
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.quantity), modifier = Modifier.weight(1f))
                            AmountPicker(value = individualQuantities, onValueChange = {individualQuantities = it })
                        }

                        OutlinedTextField(
                            value = price,
                            onValueChange = {price = it},
                            label = { Text(text = stringResource(R.string.price))},
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = vendorName,
                            onValueChange = {vendorName = it},
                            label = { Text(text = stringResource(R.string.vendor_name))},
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = location,
                            onValueChange = {location = it},
                            label = { Text(text = stringResource(R.string.location))},
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                val updatedProduct = product.copy(
                                    productName = productName,
                                    productCategory = productCategory,
                                    brandName = brandName,
                                    totalQuantity = individualQuantities,
                                    individualQuantities = mapOf(brandName to individualQuantities),
                                    price = price.toDoubleOrNull()?:0.0,
                                    vendorName = vendorName,
                                    location = location
                                )
                                viewModel.editProduct(updatedProduct){
                                    updateSuccessDialog = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.edit_product))
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
                                Text(text = stringResource(R.string.update_success))
                            },
                            text = {
                                Text(text = stringResource(R.string.product_update_success))
                            }
                        )
                    }
                }
            }
        }
    }
}