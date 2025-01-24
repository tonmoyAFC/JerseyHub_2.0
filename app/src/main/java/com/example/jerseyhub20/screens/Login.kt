package com.example.jerseyhub20.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jerseyhub20.util.Routes
import com.example.jerseyhub20.util.Status
import com.example.jerseyhub20.viewmodel.AuthViewModel


@Composable
fun LoginScreen(authViewModel: AuthViewModel, navController: NavHostController) {


    //
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }// Declare the email state variable


    val context = LocalContext.current

    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is Status.Error -> Toast.makeText(
                context,
                (authState.value as Status.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            is Status.Authenticated -> navController.navigate(Routes.Main)

            else -> Unit
        }
    }

    //
    //
    // authViewmodel.login(email, password)


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Page", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, // Bind the email state variable to the text field
            onValueChange = { email = it }, // Update the email value when the user types
            label = {
                Text("Email")
            })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = password, // Bind the email state variable to the text field
            onValueChange = { password = it }, // Update the email value when the user types
            label = {
                Text("Password")
            })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            //
            authViewModel.logIn(email, password)
            //
        }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            //
            navController.navigate(Routes.Register)
            //
        }) {
            Text(text = "Don't have an account? Sign Up")
        }
    }


}

@Preview(showSystemUi = true)
@Composable
private fun GF() {
    AdminHome(rememberNavController())
}
