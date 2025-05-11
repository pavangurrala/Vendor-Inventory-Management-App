package ie.setu.vendorinventorymanagement.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
object ThemePreferenceKeys{
    val Dark_Mode = booleanPreferencesKey("dark_mode")
}

val Context.dataStore by preferencesDataStore(name="settings")