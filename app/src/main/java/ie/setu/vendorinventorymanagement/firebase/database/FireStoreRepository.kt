package ie.setu.vendorinventorymanagement.firebase.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import ie.setu.vendorinventorymanagement.firebase.services.FirestoreService
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.data.rules.Constants.PRODUCT_COLLECTION
import ie.setu.vendorinventorymanagement.data.rules.Constants.PURCHASE_ORDER_COLLECTION
import ie.setu.vendorinventorymanagement.data.rules.Constants.USER_EMAIL
import ie.setu.vendorinventorymanagement.data.rules.Constants.BUYER_EMAIL
import ie.setu.vendorinventorymanagement.data.rules.Constants.PAYMENTS_COLLECTION
import ie.setu.vendorinventorymanagement.firebase.services.Payment
import ie.setu.vendorinventorymanagement.firebase.services.Products
import ie.setu.vendorinventorymanagement.firebase.services.PurchaseOrder
import ie.setu.vendorinventorymanagement.firebase.services.PurchaseOrdersList
import ie.setu.vendorinventorymanagement.firebase.services.PaymentsList
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class FireStoreRepository @Inject constructor(private val firestore: FirebaseFirestore
) : FirestoreService {
    private val db = FirebaseFirestore.getInstance().collection("products")

    override suspend fun addProduct(email: String, product: Product){
        val addProductWithEmail = product.copy(email = email)
        val newDocument = firestore.collection(PRODUCT_COLLECTION).document()
        val addProductWithId = addProductWithEmail.copy(id = newDocument.id)
        newDocument.set(addProductWithId).await()
    }

    override suspend fun getAll(email: String): Products {
        return firestore.collection(PRODUCT_COLLECTION)
            .whereEqualTo(USER_EMAIL, email)
            .dataObjects()
    }

    override suspend fun getProductById(productId: String): Product? {
        return firestore.collection(PRODUCT_COLLECTION).document(productId).get().await().toObject()
    }

    override suspend fun updateProduct(email: String, product: Product) {
        val editProductWithModifiedDate = product.copy(dateUpdated = Date())
        firestore.collection(PRODUCT_COLLECTION)
            .document(product.id)
            .set(editProductWithModifiedDate).await()
    }

    override suspend fun deleteProduct(email: String, productId: String) {
        firestore.collection(PRODUCT_COLLECTION)
            .document(productId)
            .delete().await()
    }

    override suspend fun getAllOrders(email: String): PurchaseOrdersList {
        return firestore.collection(PURCHASE_ORDER_COLLECTION)
            .whereEqualTo(USER_EMAIL, email)
            .dataObjects()
    }

    override suspend fun getOrderById(orderId: String): PurchaseOrder? {
        return firestore.collection(PURCHASE_ORDER_COLLECTION).document(orderId).get().await().toObject()
    }

    override suspend fun addPurchaseOrder(email: String, purchaseOrder: PurchaseOrder) {
        val addPurchaseOrderWithEmail = purchaseOrder.copy(email = email)
        val newDocument = firestore.collection(PURCHASE_ORDER_COLLECTION).document()
        val addPurchaseOrderWithId = addPurchaseOrderWithEmail.copy(orderId = newDocument.id)
        newDocument.set(addPurchaseOrderWithId).await()
    }

    override suspend fun updatePurchaseOrder(email: String, purchaseOrder: PurchaseOrder) {
        val editOrderWithModifiedDate = purchaseOrder.copy(orderModifiedDate = Date())
        firestore.collection(PURCHASE_ORDER_COLLECTION)
            .document(purchaseOrder.orderId)
            .set(editOrderWithModifiedDate).await()
    }
    override suspend fun deleteOrder(email: String, orderId: String) {
        firestore.collection(PURCHASE_ORDER_COLLECTION)
            .document(orderId)
            .delete().await()
    }

    override suspend fun getAllPayments(email: String): PaymentsList {
        return firestore.collection(PAYMENTS_COLLECTION)
            .whereEqualTo(BUYER_EMAIL, email)
            .dataObjects()
    }

    override suspend fun addPayment(email: String, payment: Payment) {
        val addPaymentWithEmail = payment.copy(buyerEmail = email)
        val newDocument = firestore.collection(PAYMENTS_COLLECTION).document()
        val addPaymentWithId = addPaymentWithEmail.copy(paymentId = newDocument.id)
        newDocument.set(addPaymentWithId).await()
    }

}