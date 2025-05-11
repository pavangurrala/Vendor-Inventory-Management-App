package ie.setu.vendorinventorymanagement.ui.screens.profile
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import ie.setu.vendorinventorymanagement.ui.screens.login.LoginViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import ie.setu.vendorinventorymanagement.navigation.Profile
import ie.setu.vendorinventorymanagement.ui.components.general.BottomAppBarProvider
import ie.setu.vendorinventorymanagement.ui.components.general.TopAppBarProvider
import ie.setu.vendorinventorymanagement.navigation.allDestinations
import ie.setu.vendorinventorymanagement.ui.components.general.MenuItem
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import ie.setu.vendorinventorymanagement.ui.screens.darkmode.DarkModeViewModel
@Composable
fun ProfileScreen(modifier: Modifier = Modifier,navController: NavHostController, darkModeViewModel: DarkModeViewModel = hiltViewModel()){
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email?:"Not available"
    val username = user?.displayName?:"Not set"
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(MenuItem.StockTracking) }
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Profile
    val isDarkMode by darkModeViewModel.isDarkMode.collectAsState()

        Scaffold(
            topBar = {
                TopAppBarProvider(
                    currentScreen = currentBottomScreen,
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
            ) {
                Text("Profile", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
//                Row(verticalAlignment = Alignment.CenterVertically){
//                    Text("Dark Mode")
//                    Switch(
//                        checked = isDarkMode,
//                        onCheckedChange = {darkModeViewModel.setDarkMode(it)}
//                    )
//                }
                Image(
                    painter = painterResource(id = R.drawable.aboutus_homer),
                    contentDescription = "Profile Page",
                    modifier = Modifier.size(300.dp)

                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Username", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("E-mail", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Dark Mode", fontSize = 18.sp)

                    }
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(username, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(email, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = {darkModeViewModel.setDarkMode(it)}
                        )

                    }
                }
//                Spacer(modifier = Modifier.height(24.dp))
//                Text(username, fontSize = 20.sp)
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(email, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }) {
                    Text("Logout")
                }

            }

        }

}
