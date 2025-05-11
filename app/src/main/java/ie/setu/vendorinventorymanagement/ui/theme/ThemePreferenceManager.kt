package ie.setu.vendorinventorymanagement.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import ie.setu.vendorinventorymanagement.ui.theme.ThemePreferenceKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferenceManager(private val context: Context) {
    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map{preferences -> preferences[ThemePreferenceKeys.Dark_Mode]?:false}
    suspend fun setDarkMode(enabled:Boolean){
        context.dataStore.edit { preferences ->
            preferences[ThemePreferenceKeys.Dark_Mode] = enabled
        }
    }
}