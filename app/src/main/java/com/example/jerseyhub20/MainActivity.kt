package com.example.jerseyhub20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jerseyhub20.screens.AdminHome
import com.example.jerseyhub20.screens.DatabaseHelper
import com.example.jerseyhub20.screens.HomeScreen
import com.example.jerseyhub20.screens.LoginScreen
import com.example.jerseyhub20.screens.SignUpScreen
import com.example.jerseyhub20.ui.theme.JerseyHub20Theme
import com.example.jerseyhub20.util.Routes
import com.example.jerseyhub20.util.Status
import com.example.jerseyhub20.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //
            val authViewModel: AuthViewModel = viewModel()
            JerseyHub20Theme {
                App(authViewModel)
            }
            //
        }
    }
}


//

@Composable
fun App(authViewModel: AuthViewModel) {

    val navController = rememberNavController()

    val authState = authViewModel.authState.observeAsState()


    NavHost(navController, startDestination = Routes.Splash) {

        composable<Routes.Splash> {
            LaunchedEffect(
                authState.value
            ) {
                if (authState.value == Status.Authenticated) {
                    delay(1000)
                    navController.navigate(Routes.Main) {
                        popUpTo(Routes.Splash) {
                            inclusive = true
                        }
                    }
                } else {
                    delay(1000)
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Splash) {
                            inclusive = true
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        composable<Routes.Login> {
            LoginScreen(authViewModel = authViewModel, navController = navController)
        }
        composable<Routes.Register> {
            SignUpScreen(authViewModel = authViewModel, navController = navController)
        }

        composable<Routes.Main> {
            LaunchedEffect(
                authState.value
            ) {
                if (authState.value == Status.NotAuthenticated) {
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Main) {
                            inclusive = true
                        }
                    }
                }
            }

            HomeScreen(navController = navController, authViewModel = authViewModel)

        }


    }
}

//