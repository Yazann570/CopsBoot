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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.copsboot.android.api.ApiClient
import com.example.copsboot.android.ui.home.HomeScreen
import com.example.copsboot.android.ui.login.LoginUiState
import com.example.copsboot.android.ui.login.LoginViewModel
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import com.example.copsboot.android.model.UserDto

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
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel()
){
    val uiState = loginViewModel.uiState

    if(uiState.isLoggedIn){
        HomeScreen(
            currentUser = uiState.currentUser,
            onLogoutClicked = loginViewModel::logout
        )
    }else{
        LoginContent(
            uiState = uiState,
            onEmailChanged = loginViewModel::onEmailChanged,
            onPasswordChanged = loginViewModel::onPasswordChanged,
            onLoginClicked = loginViewModel::login,
            onFillTestOfficerClicked = loginViewModel::fillTestOfficer
        )
    }
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onFillTestOfficerClicked: () -> Unit
){
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
                    value = uiState.email,
                    onValueChange = onEmailChanged,
                    label = {Text("Email")},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChanged,
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
                    onClick = onLoginClicked,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ){
                    if(uiState.isLoading)
                        CircularProgressIndicator()
                    else
                        Text("Login")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onFillTestOfficerClicked,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text("Fill test officer")
                }
                if (uiState.message.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    MaterialTheme{
        LoginContent(
            uiState = LoginUiState(
                email = "officer@example.com",
                password = "officer",
                message = "Login successful"
            ),
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            onFillTestOfficerClicked = {}
        )
    }
}
