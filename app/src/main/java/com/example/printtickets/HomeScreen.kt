package com.example.printtickets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.printtickets.navigation.Routes

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        NavigationButton(modifier = Modifier, navController = navController)
    }) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            NavHost(navController = navController, startDestination = Routes.ScreenTicket.route) {
                composable(route = Routes.ScreenBluetooth.route) {
                    BluetoothScreen()
                }
                composable(route = Routes.ScreenTicket.route) {
                    TicketScreen()
                }
            }
        }
    }
}

@Composable
private fun NavigationButton(modifier: Modifier, navController: NavController) {
    var currentItem by remember { mutableStateOf("ticket") }

    NavigationBar(modifier = modifier, windowInsets = NavigationBarDefaults.windowInsets) {
        NavigationBarItem(
            selected = currentItem == "bluetooth",
            onClick = {
                currentItem = "bluetooth"
                navController.navigate(Routes.ScreenBluetooth.route)
            },
            icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = null) },
            label = { Text(text = "Bluetooth") }
        )
        NavigationBarItem(
            selected = currentItem == "ticket",
            onClick = {
                currentItem = "ticket"
                navController.navigate(Routes.ScreenTicket.route)
            },
            icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null) },
            label = { Text(text = "Ticket") }
        )
    }
}