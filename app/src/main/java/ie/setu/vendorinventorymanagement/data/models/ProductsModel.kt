package ie.setu.vendorinventorymanagement.data.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
@Entity
data class ProductsModel(
    val id: String = "N/A",
    val productName: String = "N/A",
    val productCategory: String = "N/A",
    val totalQuantity: Int = 0,
    val vendorName: String = "N/A",
    val location: String = "N/A",
    val brandName: String = "N/A",
    val email : String = FirebaseAuth.getInstance().currentUser?.email.toString(),
    val dateAdded : Date = Date(),
    val dateUpdated: Date = Date(),
    val individualQuantities: Map<String, Int> = emptyMap(),
    val price: Double = 0.0
)
