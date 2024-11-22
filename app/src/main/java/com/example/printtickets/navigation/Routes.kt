package com.example.printtickets.navigation

sealed class Routes(val route: String) {
    data object ScreenBluetooth: Routes("screenBluetooth")
    data object ScreenTicket: Routes("screenTicket")
}