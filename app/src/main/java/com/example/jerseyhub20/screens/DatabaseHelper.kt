package com.example.jerseyhub20.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DatabaseHelper(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }

}

@Preview(showSystemUi = true)
@Composable
private fun Arr() {
    DatabaseHelper()
}