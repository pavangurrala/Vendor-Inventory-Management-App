package ie.setu.vendorinventorymanagement.firebase.services
import ie.setu.vendorinventorymanagement.data.models.ProductsModel
import kotlinx.coroutines.flow.Flow
typealias Product = ProductsModel
typealias Products = Flow<List<Product>>
interface FirestoreService {
    suspend fun getAll(email:String):Products
    suspend fun addProduct(email:String, product:Product)
}