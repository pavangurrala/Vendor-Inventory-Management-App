package ie.setu.vendorinventorymanagement.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ie.setu.vendorinventorymanagement.firebase.database.FireStoreRepository
import ie.setu.vendorinventorymanagement.firebase.services.FirestoreService
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    fun provideFirebaseFirestore()
            : FirebaseFirestore = Firebase.firestore
    @Provides
    fun provideFirestoreRepository(
        firebaseFirestore: FirebaseFirestore
    ) : FirestoreService = FireStoreRepository(
        firestore = firebaseFirestore
    )
}