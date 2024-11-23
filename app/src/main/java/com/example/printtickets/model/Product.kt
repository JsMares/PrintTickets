package com.example.printtickets.model

data class Product(val name: String, val price: Double, val quantity: Int) {
    companion object {
        val products = listOf(
            Product("Laptop", 12.00, 5),
            Product("Monitor", 51.00, 1),
            Product("Teclado", 4.00, 2),
            Product("Iphone", 150.00, 6),
            Product("Tablet", 21.00, 1),
            Product("Mochila", 3.00, 5)
        )
    }
}
