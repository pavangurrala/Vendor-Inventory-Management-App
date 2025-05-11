package ie.setu.vendorinventorymanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ie.setu.vendorinventorymanagement.navigation.AppNavgraph
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import ie.setu.vendorinventorymanagement.navigation.allDestinations
import ie.setu.vendorinventorymanagement.navigation.StockTracking
import ie.setu.vendorinventorymanagement.ui.components.general.BottomAppBarProvider
import ie.setu.vendorinventorymanagement.data.models.ProductsModel
import ie.setu.vendorinventorymanagement.ui.screens.darkmode.DarkModeViewModel
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkModeViewModel: DarkModeViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val isDarkMode by darkModeViewModel.isDarkMode.collectAsState()
            VendorInventoryManagementTheme(darkTheme = isDarkMode) {

                Surface(color = MaterialTheme.colorScheme.background) {
                    VendorManagementApp(modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun VendorManagementApp(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {

    Scaffold(
        modifier = modifier,
        content = {paddingValues ->
            AppNavgraph(
                modifier = modifier,
                navController = navController,
                paddingValues = paddingValues,

            )
        },


    )

}

