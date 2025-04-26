package ie.setu.vendorinventorymanagement.data.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

@Entity
data class PurchaseOrdersModel(
    val orderId: String = "N/A",
    val productId: String = "N/A",
    val location: String = "N/A",
    val brandName: String = "N/A",
    val email : String = FirebaseAuth.getInstance().currentUser?.email.toString(),
    val orderPlacedDate : String = "",
    val expectedDeliveryDate: String = "N/A",
    val productName: String = "N/A",
    val vendorName: String = "N/A",
    val orderPlaced: String = "yes",
    val orderedQuantity: Int = 0,
    val totalCost: Double = 0.0,
    val destination: String ="N/A",
    val buyerEmail: String = "N/A",
    val buyerPhoneNumber: String = "N/A",
    val orderModifiedDate: Date = Date(),
    val payment: String = "Not Done",
    )