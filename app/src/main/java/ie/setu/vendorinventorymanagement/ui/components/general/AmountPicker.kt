package ie.setu.vendorinventorymanagement.ui.components.general

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.NumberPicker

@Composable
fun AmountPicker(value:Int, maximumValue:Int, onValueChange: (Int) -> Unit){
    AndroidView(
        factory = {context ->
            NumberPicker(context).apply {
                minValue = 1
                maxValue = maximumValue
                this.value = value
                setOnValueChangedListener{_,_,newValue ->
                    onValueChange(newValue)
                }
            }
        },
        update = {it.value = value}

    )
}