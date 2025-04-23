package ie.setu.vendorinventorymanagement.ui.components.general
import androidx.collection.emptyLongSet
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import ie.setu.vendorinventorymanagement.navigation.AppDestination
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.setu.vendorinventorymanagement.navigation.StockTracking
import ie.setu.vendorinventorymanagement.ui.components.general.DropDownMenu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarProvider(
    currentScreen: AppDestination,
    canNavigateBack: Boolean,
    navigateUp:()->Unit ={}
){
    TopAppBar(
        title = {
            Text(
                text = currentScreen.label,
                color = Color.White
            )
        },
        windowInsets = WindowInsets(0),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            if(canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }else
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu Button",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )

        },
        actions = { DropDownMenu()}
    )
}
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview(){
    VendorInventoryManagementTheme {
        TopAppBarProvider(
            StockTracking,
            canNavigateBack = true
        )
    }
}
