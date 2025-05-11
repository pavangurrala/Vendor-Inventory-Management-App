package ie.setu.vendorinventorymanagement.ui.screens.productmanagement

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.ui.components.general.AmountPicker
import ie.setu.vendorinventorymanagement.ui.components.general.DropDownField

@Composable
fun AddProductScreen(modifier: Modifier = Modifier,navController: NavHostController, viewModel: ProductManagementViewModel = hiltViewModel()){
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = linkingScreens.find { it.route == currentDestination?.route }?:Home
    var addProductSuccessDialog by remember { mutableStateOf(false) }

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
            ){
                paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                ){
                val context = LocalContext.current
                var productName by remember { mutableStateOf("") }
                var productCategory by remember { mutableStateOf("Laptops") }
                var brandName by remember { mutableStateOf("Dell") }
                var price by remember { mutableStateOf("") }
                var vendorName by remember { mutableStateOf("") }
                var location by remember { mutableStateOf("") }
                var individualQuantities by remember { mutableIntStateOf(1) }
                val brandOptions = listOf("Dell", "HP", "Apple", "Samsung", "Lenovo")
                val categories = listOf("Laptops", "Phones", "Printers")

                val brands = remember { mutableStateListOf<BrandNameModel>()}

                val totalProductQuantity = individualQuantities
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
                        onValueChange = {productName = it},
                        label = { Text(text = stringResource(R.string.product_name))},
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropDownField("Category",productCategory, categories ){productCategory = it}
                    DropDownField("Brand",brandName, brandOptions ){brandName = it}
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.quantity), modifier = Modifier.weight(1f))
                        AmountPicker(value = individualQuantities,maximumValue=100, onValueChange = {individualQuantities = it })
                    }
                    OutlinedTextField(
                        value = price,
                        onValueChange = {price = it},
                        label = { Text(text = stringResource(R.string.price))},
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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
                            val product = Product(
                                productName = "$brandName - $productName",
                                productCategory = productCategory,
                                brandName = brandName,
                                totalQuantity = individualQuantities,
                                individualQuantities = mapOf(brandName to individualQuantities),
                                price = price.toDoubleOrNull()?:0.0,
                                vendorName = vendorName,
                                location = location
                            )
                            viewModel.addProducts(product){
                                addProductSuccessDialog = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.add_product))
                    }

                }
                if (addProductSuccessDialog) {
                    AlertDialog(
                        onDismissRequest = { addProductSuccessDialog = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    addProductSuccessDialog = false
                                    navController.popBackStack()
                                }
                            ) {
                                Text("Ok")
                            }
                        },
                        title = {
                            Text(text = stringResource(R.string.add_success))
                        },
                        text = {
                            Text(text = stringResource(R.string.product_addition_success))
                        }
                    )
                }
            }
        }

}