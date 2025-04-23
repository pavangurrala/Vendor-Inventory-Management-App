package ie.setu.vendorinventorymanagement.main
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import com.google.firebase.Firebase
import com.google.firebase.initialize

@HiltAndroidApp
class VendorInventoryManagementMainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Starting Vendor Inventory Management Application")
        Firebase.initialize(context = this)
    }
}