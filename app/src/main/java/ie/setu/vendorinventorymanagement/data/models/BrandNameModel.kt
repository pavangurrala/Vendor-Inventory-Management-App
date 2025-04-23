package ie.setu.vendorinventorymanagement.data.models
import androidx.room.Entity
@Entity
data class BrandNameModel (
    var brandName: String = "",
    var quantity:Int = 0,
//    val price: Double = 0.0
)

