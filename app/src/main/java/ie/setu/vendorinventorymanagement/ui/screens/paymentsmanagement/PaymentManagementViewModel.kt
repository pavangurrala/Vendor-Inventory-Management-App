package ie.setu.vendorinventorymanagement.ui.screens.paymentsmanagement
import androidx.compose.runtime.getValue
import ie.setu.vendorinventorymanagement.firebase.database.FireStoreRepository
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.firebase.services.PurchaseOrder
import ie.setu.vendorinventorymanagement.firebase.services.Payment
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
import ie.setu.vendorinventorymanagement.data.models.PaymentsModel
import kotlinx.coroutines.flow.MutableStateFlow
@HiltViewModel
class PaymentManagementViewModel@Inject constructor(
    private val auth: FirebaseAuth, private val repository: FirestoreService
):ViewModel()
{
    private val _orders = MutableStateFlow<List<PurchaseOrdersModel>>(emptyList())
    private val _editOrder = MutableStateFlow<PurchaseOrder?>(null)
    private val _payments = MutableStateFlow<List<PaymentsModel>>(emptyList())
    val uiPayments:StateFlow<List<PaymentsModel>> = _payments.asStateFlow()
    private val _editPayments = MutableStateFlow<Payment?>(null)
    var iserror = mutableStateOf(false)
    var isloading = mutableStateOf(false)
    var error = mutableStateOf(Exception())

    init {
        getAllPayments()
    }
    fun getAllPayments(){
        viewModelScope.launch {
            try{
                isloading.value = true
                repository.getAllPayments(auth.currentUser?.email.toString()).collect{ items ->
                    _payments.value = items
                    iserror.value = false
                    isloading.value = false
                }
                Timber.i("DVM RVM = : ${_payments.value}")
            }
            catch(e:Exception){
                iserror.value = true
                isloading.value = false
                error.value = e
                Timber.i("RVM Error ${e.message}")
            }
        }
    }

    fun addPayment(paymentsModel: PaymentsModel,purchaseOrdersModel: PurchaseOrdersModel, onComplete: () -> Unit = {}){
        viewModelScope.launch{
            try{
                isloading.value = true
                repository.addPayment(auth.currentUser?.email.toString(), paymentsModel)
                repository.updatePurchaseOrder(auth.currentUser?.email.toString(), purchaseOrdersModel)
                iserror.value = false
                isloading.value = false
                onComplete()
            }catch (e:Exception){
                iserror.value = true
                error.value = e
                isloading.value = false
            }
            Timber.i("DVM Insert Message = : ${error.value.message} and isError ${iserror.value}")
        }
    }
}