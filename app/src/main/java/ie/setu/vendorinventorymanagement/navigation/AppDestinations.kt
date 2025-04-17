package ie.setu.vendorinventorymanagement.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.sharp.Assignment
import androidx.compose.material.icons.automirrored.sharp.ReceiptLong
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.sharp.Assessment
import androidx.compose.material.icons.sharp.ConnectingAirports
import androidx.compose.material.icons.sharp.Dashboard
import androidx.compose.ui.graphics.vector.ImageVector
interface AppDestination {
    val icon: ImageVector
    val label: String
    val route: String
}
object Login : AppDestination {
    override val icon = Icons.AutoMirrored.Filled.Login
    override val label = "Login"
    override val route = "login"
}
object SignUp : AppDestination {
    override val icon = Icons.AutoMirrored.Filled.Login
    override val label = "SignUp"
    override val route = "signup"
}
object ProductManagement : AppDestination {
    override val icon = Icons.Sharp.Dashboard
    override val label = "Dashboard"
    override val route = "productmanagement"
}
object StockTracking : AppDestination{
    override val icon = Icons.Sharp.Assessment
    override val label = "StockTracking"
    override val route = "stocktracking"
}

val bottomAppBarDestinations = listOf(ProductManagement,StockTracking )
val allDestinations = listOf(ProductManagement, StockTracking)