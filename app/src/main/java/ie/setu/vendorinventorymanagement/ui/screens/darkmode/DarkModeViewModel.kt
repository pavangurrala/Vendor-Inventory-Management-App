package ie.setu.vendorinventorymanagement.ui.screens.darkmode
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import ie.setu.vendorinventorymanagement.ui.theme.ThemePreferenceManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

@HiltViewModel
class DarkModeViewModel @Inject constructor(
   @ApplicationContext private val context: Context
): ViewModel() {
    companion object{
        private val Context.dataStore by preferencesDataStore(name="settings")
        private val Dark_Mode_Key = booleanPreferencesKey("dark_mode")
    }

    val isDarkMode: StateFlow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[Dark_Mode_Key]?:false }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    fun setDarkMode(enabled:Boolean){
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[Dark_Mode_Key] = enabled
            }
        }
    }
}