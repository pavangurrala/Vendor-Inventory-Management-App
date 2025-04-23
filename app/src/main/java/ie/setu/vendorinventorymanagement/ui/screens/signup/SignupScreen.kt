package ie.setu.vendorinventorymanagement.ui.screens.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import ie.setu.vendorinventorymanagement.ui.screens.login.LoginViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.ui.theme.VendorInventoryManagementTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun SignupScreen(navController: NavController, auth: FirebaseAuth = FirebaseAuth.getInstance()){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf("") }
    VendorInventoryManagementTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                Image(
                    painter = painterResource(id = R.drawable.vshare),
                    contentDescription = "VShare Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 24.dp)
                )
                OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value=email, onValueChange = {email=it}, label={Text("Email")})
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    auth.createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener { result ->
                            val profileUpdate = UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build()
                            result.user?.updateProfile(profileUpdate)
                                ?.addOnCompleteListener{
                                    navController.navigate("login") }
                                }

                        .addOnFailureListener{error = it.message.toString()}
                }){
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(8.dp))
                if(error !=null) Text(error?:"", color = Color.Red)
                Text("Already have an account? Login", Modifier.clickable {
                    navController.popBackStack()
                })
            }
        }
    }

}