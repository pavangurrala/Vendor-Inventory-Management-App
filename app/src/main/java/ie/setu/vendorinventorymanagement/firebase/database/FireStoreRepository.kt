package ie.setu.vendorinventorymanagement.firebase.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import ie.setu.vendorinventorymanagement.firebase.services.FirestoreService
import ie.setu.vendorinventorymanagement.firebase.services.Product
import ie.setu.vendorinventorymanagement.data.rules.Constants.PRODUCT_COLLECTION
import ie.setu.vendorinventorymanagement.data.rules.Constants.USER_EMAIL
import ie.setu.vendorinventorymanagement.firebase.services.Products
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

}