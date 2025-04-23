package ie.setu.vendorinventorymanagement.ui.components.general
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import ie.setu.vendorinventorymanagement.data.models.BrandNameModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.setValue
import androidx.room.util.TableInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.*
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuantityPicker(
    brandViewModel : BrandNameModel,
    brandOptions: List<String>,
    onRemove:()->Unit
){
    var brandExpanded by remember { mutableStateOf(false) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            ExposedDropdownMenuBox(
                expanded = brandExpanded,
                onExpandedChange = {brandExpanded =!brandExpanded}
            ) {
                OutlinedTextField(
                    value = brandViewModel.brandName,
                    onValueChange = {},
                    label = {Text("Brand")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded)
                    },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().weight(1f)
                )
                ExposedDropdownMenu(expanded = brandExpanded, onDismissRequest = {brandExpanded = false}) {
                    brandOptions.forEach{option ->
                        DropdownMenuItem(
                            text = {Text(option)},
                            onClick = {
                                brandViewModel.brandName = option
                                brandExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            AndroidView(
                factory = {context ->
                    android.widget.NumberPicker(context).apply {
                        minValue = 1
                        maxValue = 100
                        value = brandViewModel.quantity
                        setOnValueChangedListener{_,_,newVal->
                            brandViewModel.quantity = newVal
                        }
                    }
                },
                update = {it.value = brandViewModel.quantity},
                modifier = Modifier.width(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {onRemove()}) {
                Icon(Icons.Default.Delete, contentDescription = "Remove Brand")
            }
        }
    }
}
