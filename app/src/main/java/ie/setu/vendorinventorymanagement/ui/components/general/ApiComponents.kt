package ie.setu.vendorinventorymanagement.ui.components.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme

@Composable
fun ShowError(headline: String, subtitle: String, onClick: ((Unit) -> Unit)) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.graphicsLayer(scaleY = 2.5f, scaleX = 2.5f),
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color.Red
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = headline,
                fontSize = 20.sp,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = subtitle,
                fontSize = 16.sp,
            )
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = { onClick.invoke(Unit) }) {
            Text("Retry")
        }
    }
}

@Composable
fun ShowRefreshList(modifier: Modifier = Modifier,
                    onClick: ((Unit) -> Unit)) {
    Column(
        modifier = modifier.padding(
            top = 1.dp,
            bottom = 1.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)) {

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = { onClick.invoke(Unit) }) {
            Text("Refresh")
        }
    }
}

@Composable
fun ShowRefresh(onClick: ((Unit) -> Unit)) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 32.dp,
                bottom = 4.dp
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .align(Alignment.CenterHorizontally),
                onClick = { onClick.invoke(Unit) }) {
                Text("Refresh")
            }
        }
    }
}

@Composable
fun ShowLoader(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(message,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun UtilsPreview() {
    VendorInventoryManagementTheme {
        ShowError(headline = "Main Error Message",
            subtitle = "more error information", onClick = {})
        ShowLoader("Trying to Donate...")
        //   ShowRefreshList(onClick = {})
    }
}