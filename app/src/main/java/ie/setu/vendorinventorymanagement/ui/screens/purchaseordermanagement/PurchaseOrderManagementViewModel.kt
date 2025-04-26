package ie.setu.vendorinventorymanagement.ui.screens.purchaseordermanagement
import androidx.compose.runtime.getValue
import ie.setu.vendorinventorymanagement.firebase.database.FireStoreRepository
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.firebase.services.PurchaseOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ie.setu.vendorinventorymanagement.firebase.services.FirestoreService
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import ie.setu.vendorinventorymanagement.data.models.ProductsModel
import ie.setu.vendorinventorymanagement.data.models.PurchaseOrdersModel
import kotlinx.coroutines.flow.MutableStateFlow
@HiltViewModel
class PurchaseOrderManagementViewModel@Inject constructor(
    private val auth: FirebaseAuth, private val repository: FirestoreService
):ViewModel() {
    private val _products = MutableStateFlow<List<ProductsModel>>(emptyList())
    private val _editProduct = MutableStateFlow<Product?>(null)
    private val _orders = MutableStateFlow<List<PurchaseOrdersModel>>(emptyList())
    private val _editOrder = MutableStateFlow<PurchaseOrder?>(null)
    val uiProducts: StateFlow<List<ProductsModel>> = _products.asStateFlow()
    val editProducts : StateFlow<Product?> = _editProduct
    val uiOrders:StateFlow<List<PurchaseOrdersModel>> = _orders.asStateFlow()
    val editOrders : StateFlow<PurchaseOrder?> = _editOrder
    var iserror = mutableStateOf(false)
    var isloading = mutableStateOf(false)
    var error = mutableStateOf(Exception())
    init {
        getProducts()
        getAllOrders()
    }
    fun getProducts(){
        viewModelScope.launch {
            try{
                isloading.value = true
                repository.getAll(auth.currentUser?.email.toString()).collect{ items ->
                    _products.value = items
                    iserror.value = false
                    isloading.value = false
                }
                Timber.i("DVM RVM = : ${_products.value}")
            }
            catch(e:Exception){
                iserror.value = true
                isloading.value = false
                error.value = e
                Timber.i("RVM Error ${e.message}")
            }
        }
    }
    fun getProductById(productId: String){
        viewModelScope.launch {
            try{
                isloading.value = true
                val exItem = repository.getProductById(productId)
                _editProduct.value = exItem
                iserror.value = false
                isloading.value = false
            }catch(e:Exception){
                iserror.value = true
                isloading.value = false
                error.value = e
                Timber.i("RVM Error ${e.message}")
            }
        }
    }
    fun getOrderById(orderId:String){
        viewModelScope.launch {
            try{
                isloading.value = true
                val exOrder = repository.getOrderById(orderId)
                _editOrder.value = exOrder
                iserror.value = false
                isloading.value = false
            }catch(e:Exception){
                iserror.value = true
                isloading.value = false
                error.value = e
                Timber.i("RVM Error ${e.message}")
            }
        }
    }
    fun getAllOrders(){
        viewModelScope.launch {
            try{
                isloading.value = true
                repository.getAllOrders(auth.currentUser?.email.toString()).collect{ items ->
                    _orders.value = items
                    iserror.value = false
                    isloading.value = false
                }
                Timber.i("DVM RVM = : ${_orders.value}")
            }
            catch(e:Exception){
                iserror.value = true
                isloading.value = false
                error.value = e
                Timber.i("RVM Error ${e.message}")
            }
        }
    }
    fun addPurchaseOrder(purchaseOrder: PurchaseOrdersModel, updatedProduct: ProductsModel ,onComplete:()->Unit = {}){
        viewModelScope.launch {
            try{
                isloading.value = true
                repository.updateProduct(auth.currentUser?.email.toString(), updatedProduct)
                repository.addPurchaseOrder(auth.currentUser?.email.toString(), purchaseOrder)
                iserror.value = false
                isloading.value = false
                onComplete()
            }catch(e:Exception){
                iserror.value = true
                error.value = e
                isloading.value = false
            }
            Timber.i("DVM Insert Message = : ${error.value.message} and isError ${iserror.value}")
        }
    }
    fun updateOrder(purchaseOrder: PurchaseOrdersModel, onComplete: () -> Unit){
        viewModelScope.launch {
            try{
                isloading.value = true
                repository.updatePurchaseOrder(purchaseOrder.email, purchaseOrder)
                iserror.value = false
                isloading.value = false
                onComplete()
            }catch (e:Exception){
                iserror.value = true
                error.value = e
                isloading.value = false
            }
        }
    }
    fun deleteOrder(purchaseOrder: PurchaseOrdersModel) = viewModelScope.launch {
        repository.deleteOrder(purchaseOrder.email, purchaseOrder.orderId)
    }
}