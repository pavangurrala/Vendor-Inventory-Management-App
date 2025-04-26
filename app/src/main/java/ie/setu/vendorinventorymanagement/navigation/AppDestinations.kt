package ie.setu.vendorinventorymanagement.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.sharp.Assignment
import androidx.compose.material.icons.automirrored.sharp.ReceiptLong
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Assessment
import androidx.compose.material.icons.sharp.ConnectingAirports
import androidx.compose.material.icons.sharp.Dashboard
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material.icons.sharp.ShoppingCart
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
    override val icon = Icons.Filled.Inventory
    override val label = "Products "
    override val route = "productmanagement"
}
object PurchaseOrderManagement : AppDestination {
    override val icon = Icons.Filled.ShoppingCart
    override val label = "Buy Products"
    override val route = "purchaseordermanagement"
}
object PaymentManagement : AppDestination {
    override val icon = Icons.Filled.Paid
    override val label = "Payments"
    override val route = "paymentmanagement"
}
object StockTracking : AppDestination{
    override val icon = Icons.AutoMirrored.Filled.List
    override val label = "Stock Tracking"
    override val route = "stocktracking"
}
object Home : AppDestination{
    override val icon = Icons.Sharp.Home
    override val label = "Home"
    override val route = "home"
}
object Profile : AppDestination{
    override val icon = Icons.Sharp.Person
    override val label = "Profile"
    override val route = "profile"
}
object AddProduct: AppDestination{
    override val icon = Icons.Sharp.Add
    override val label = "Add Product"
    override val route = "add_product"
}
object EditProduct: AppDestination{
    override val icon = Icons.Sharp.Edit
    override val label = "Edit Product"
    override val route = "edit_product/{productId}"
}
object PlaceOrder: AppDestination{
    override val icon = Icons.Sharp.ShoppingCart
    override val label = "Place Order"
    override val route = "place_order/{productId}"
}
object EditOrder: AppDestination{
    override val icon = Icons.Sharp.Edit
    override val label = "Edit Order"
    override val route = "edit_order/{orderId}"
}
object Orders: AppDestination{
    override val icon = Icons.Default.ShoppingBag
    override val label = "Orders"
    override val route = "purchased_orders"
}
object AddPayments: AppDestination{
    override val icon = Icons.Default.Payment
    override val label = "AddPayments"
    override val route = "pay_order/{orderId}"
}

object Maps: AppDestination{
    override val icon = Icons.Default.LocationOn
    override val label = "Maps"
    override val route = "maps"
}


val bottomAppBarDestinations = listOf(Home,Orders,PaymentManagement,Profile)
val allDestinations = listOf(Home,Orders, PaymentManagement,Profile)
val listOfHomeTiles = listOf(ProductManagement, StockTracking, PurchaseOrderManagement, PaymentManagement, Orders, Maps)
val linkingScreens = listOf(AddProduct,EditProduct,PlaceOrder, EditOrder,AddPayments)