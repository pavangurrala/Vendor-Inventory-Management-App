package ie.setu.vendorinventorymanagement.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
interface AppDestination {
    val icon: ImageVector
    val label: String
    val route: String
}
object ProductManagement : AppDestination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val label = "ProductManagement"
    override val route = "productmanagement"
}