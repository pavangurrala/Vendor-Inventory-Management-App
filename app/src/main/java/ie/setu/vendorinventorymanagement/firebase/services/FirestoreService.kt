package ie.setu.vendorinventorymanagement.firebase.services
import ie.setu.vendorinventorymanagement.data.models.ProductsModel
import ie.setu.vendorinventorymanagement.data.models.PurchaseOrdersModel
import ie.setu.vendorinventorymanagement.data.models.PaymentsModel
import kotlinx.coroutines.flow.Flow
typealias Product = ProductsModel
typealias Products = Flow<List<Product>>
typealias PurchaseOrder = PurchaseOrdersModel
typealias PurchaseOrdersList = Flow<List<PurchaseOrder>>
typealias Payment = PaymentsModel
typealias PaymentsList = Flow<List<Payment>>
interface FirestoreService {
    suspend fun getAll(email:String):Products
    suspend fun getProductById(productId:String): Product?
    suspend fun addProduct(email:String, product:Product)
    suspend fun updateProduct(email:String, product:Product)
    suspend fun deleteProduct(email:String, productId:String)
    suspend fun getAllOrders(email: String): PurchaseOrdersList
    suspend fun getOrderById(orderId: String):PurchaseOrder?
    suspend fun addPurchaseOrder(email: String, purchaseOrder: PurchaseOrder)
    suspend fun updatePurchaseOrder(email: String, purchaseOrder: PurchaseOrder)
    suspend fun deleteOrder(email: String, orderId: String)
    suspend fun getAllPayments(email: String):PaymentsList
    suspend fun addPayment(email: String, payment: Payment)
}