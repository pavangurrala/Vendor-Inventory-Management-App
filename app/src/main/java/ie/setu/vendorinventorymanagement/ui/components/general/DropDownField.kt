package ie.setu.vendorinventorymanagement.ui.components.general

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownField(label:  String, selected: String, options: List<String>, onSelect: (String)-> Unit){
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded =!expanded}) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = {Text(label)},
            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}
        ) {
            options.forEach{option ->
                DropdownMenuItem(
                    text = {Text(option)},
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}