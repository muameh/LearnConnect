package com.mehmetbaloglu.learnconnect.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mehmetbaloglu.learnconnect.data.models.LogInState
import com.mehmetbaloglu.learnconnect.navigation.AppScreens
import com.mehmetbaloglu.learnconnect.ui.viewmodels.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val signUpState = signUpViewModel.signUpState.collectAsState()
    val signUpMessage = signUpViewModel.signUpMessage.collectAsState()

    var userNameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var confirmPasswordState by remember { mutableStateOf("") }

    LaunchedEffect(key1 = signUpState.value) {
        when (signUpState.value) {
            LogInState.Success -> {
                Toast.makeText(context, "Successfully signed up", Toast.LENGTH_SHORT).show()
                navController.navigate(AppScreens.CoursesScreen.name)
            }
            LogInState.Error -> {
                Toast.makeText(context, signUpMessage.value, Toast.LENGTH_SHORT).show()
            }
            LogInState.Loading -> {}
            LogInState.Nothing -> {}
        }
    }

    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(top = 0.dp, start = 32.dp, end = 32.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = userNameState,
                    onValueChange = { userNameState = it },
                    label = { Text(text = "User Name") },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6ac1f0))
                )
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6ac1f0))
                )
                OutlinedTextField(
                    value = passwordState,
                    onValueChange = { passwordState = it },
                    label = { Text(text = "Password") },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6ac1f0))
                )
                OutlinedTextField(
                    value = confirmPasswordState,
                    onValueChange = { confirmPasswordState = it },
                    label = { Text(text = "Confirm Password") },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6ac1f0)),
                    isError = passwordState.isEmpty() && confirmPasswordState.isEmpty() && passwordState != confirmPasswordState
                )
                if (signUpState.value == LogInState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                else {
                    Button(
                        onClick = {
                            signUpViewModel.signUpWithEmail(
                                userNameState,
                                emailState,
                                passwordState
                            )
                        },
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        enabled = passwordState.isNotEmpty() && confirmPasswordState.isNotEmpty() && passwordState == confirmPasswordState,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6ac1f0))
                    ) {
                        Text(text = "Sign Up")
                    }
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(text = "Already have an account? Log In")
                    }
                }


            }

        }

    }





}