package ie.setu.vendorinventorymanagement.ui.components.general
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DropDownMenu() {

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Help") }

    Box(
        contentAlignment = Alignment.Center,

        ) {
        // 3 vertical dots icon
        IconButton(onClick = {
            expanded = true
        }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Open Info",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(color = Color.White,text = "Info", fontSize = 18.sp) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = {
                    selectedOptionText = "Info"
                    expanded = false
                },
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DropDownMenuPreview(){
    VendorInventoryManagementTheme {
        DropDownMenu()
    }
}
