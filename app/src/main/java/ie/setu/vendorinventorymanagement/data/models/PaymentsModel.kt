package ie.setu.vendorinventorymanagement.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
@Entity
data class PaymentsModel (
    val paymentId: String = "",
    val orderId: String = "",
    val productName: String = "",
    val paymentDate: String = "",
    val paymentMethod: String = "",
    val totalCost: Double = 0.0,
    val buyerEmail: String ="",
    val paymentStatus:String = "Not Paid",
    val orderedQuantity: Int = 0
)