package ie.setu.vendorinventorymanagement.ui.screens.productmanagement
import androidx.compose.runtime.getValue
import ie.setu.vendorinventorymanagement.firebase.database.FireStoreRepository
import ie.setu.vendorinventorymanagement.firebase.services.Product
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
import ie.setu.vendorinventorymanagement.data.models.BrandNameModel
import kotlinx.coroutines.flow.MutableStateFlow


@HiltViewModel
class ProductManagementViewModel @Inject constructor(
    private val auth: FirebaseAuth, private val repository: FirestoreService
):ViewModel()
{
    private val _products = MutableStateFlow<List<ProductsModel>>(emptyList())
    val uiProducts: StateFlow<List<ProductsModel>> = _products.asStateFlow()
    var iserror = mutableStateOf(false)
    var isloading = mutableStateOf(false)
    var error = mutableStateOf(Exception())

    init {
        getProducts()
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
    fun addProducts(product: ProductsModel, onComplete:()->Unit = {}){
        viewModelScope.launch {
            try{
                isloading.value = true
                repository.addProduct(auth.currentUser?.email.toString(), product)
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
}