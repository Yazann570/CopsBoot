package com.example.copsboot.android

import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.copsboot.android.api.ApiClient
import com.example.copsboot.android.ui.theme.CopsBootAndroidTheme
import kotlinx.coroutines.launch
import java.nio.charset.Charset

private const val CLIENT_ID = "copsboot-mobile-client"
private const val CLIENT_SECRET = "ccUyb6vS4S8nxfbKPCrN"
private const val SCOPE = "mobile_app"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CopsBootApp()
        }
    }
}

@Composable
fun CopsBootApp(){
    MaterialTheme{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen(){
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ){
            Column(
                modifier = Modifier.padding(24.dp)
            ){
                Text(
                    text = "CopsBoot",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Police system mobile app",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    label = {Text("Email")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            message = ""

                            try{
                                val response = ApiClient.authApi.login(
                                    authorization = createBasicAuthHeader(),
                                    grantType = "password",
                                    username = email,
                                    password = password,
                                    scope = SCOPE
                                )
                                if(response.isSuccessful){
                                    val tokenResponse = response.body()
                                    val token = tokenResponse?.accessToken
                                    val tokenType = tokenResponse?.tokenType ?: "Bearer"
                                    if(token.isNullOrBlank()){
                                        message = "Login succeeded, but no access token was returned."
                                    } else{
                                        val authorizationHeader = "$tokenType $token"

                                        val currentUserResponse = ApiClient.userApi.getCurrentUser(
                                            authorizaton = authorizationHeader
                                        )
                                        message = if(currentUserResponse.isSuccessful){
                                            val currentUserJson = currentUserResponse.body()?.string()
                                            "Login successful.\n\nCurrent user: \n$currentUserJson"
                                        }else{
                                            "Login succeeded, but /api/users/me failed. Status code: ${currentUserResponse.code()}"
                                        }
                                    }
                                } else{
                                    message = "Login failed. Status code: ${response.code()}"
                                }
                            }catch(exception: Exception){
                                message = "Network error: ${exception.message}"
                            }finally{
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ){
                    if(isLoading)
                        CircularProgressIndicator()
                    else
                        Text("Login")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        email = "officer@example.com"
                        password = "officer"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Fill test officer")
                }
                if (message.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun createBasicAuthHeader(): String {
    val credentials = "$CLIENT_ID:$CLIENT_SECRET"
    val encodedCredentials = Base64.encodeToString(
        credentials.toByteArray(Charset.forName("UTF-8")),
        Base64.NO_WRAP
    )

    return "Basic $encodedCredentials"
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    CopsBootApp()
}
