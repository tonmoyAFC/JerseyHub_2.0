package com.example.jerseyhub20.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun CRUD(navController: NavHostController) {

    Column {
        Spacer(Modifier.height(50.dp))
        Text("Admin")


        Spacer(Modifier.height(60.dp))
        Text("Database Helper", modifier = Modifier.clickable {
           // navController.navigate("DatabaseHelperScreen")
        })
    }

}

@Preview(showSystemUi = true)
@Composable
private fun Shape() {
    AdminHome(rememberNavController())
}
