package ie.setu.vendorinventorymanagement.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import androidx.compose.foundation.Image
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val loginState = viewModel.loginState

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    VendorInventoryManagementTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Image(
                    painter = painterResource(id = R.drawable.vshare),
                    contentDescription = "VShare Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 24.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator()
                }

                Button(
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Don't have an account? Sign Up", modifier = Modifier.clickable {
                    navController.navigate("signup")
                })

                Spacer(modifier = Modifier.height(8.dp))

                Text("Forgot password?", modifier = Modifier.clickable {
                    navController.navigate("reset")
                })

                if (loginState is LoginState.Error) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(loginState.message, color = MaterialTheme.colorScheme.error)
                }

                if (loginState is LoginState.Success) {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    VendorInventoryManagementTheme {
        LoginScreen(navController = navController)
    }
}