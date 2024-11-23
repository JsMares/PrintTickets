package com.example.printtickets

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.printtickets.model.Product

@Preview(showBackground = true)
@Composable
fun TicketScreen() {
    val context = LocalContext.current
    val dataStore = StoreDevice(context)
    var device by remember { mutableStateOf<BluetoothDevice?>(null) }
    val bluetooth = BluetoothCustom()
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        dataStore.getDevice.collect { collectedDevice ->
            device = collectedDevice
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp)
        ) {
            Text(
                text = stringResource(id = R.string.title_tickets_screen),
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Spacer(modifier = Modifier.size(20.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.product_name)) }
            )
            Spacer(modifier = Modifier.size(12.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.product_price)) }
            )
            Spacer(modifier = Modifier.size(12.dp))
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.product_quantity)) }
            )
            Spacer(modifier = Modifier.size(12.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = stringResource(id = R.string.add_product))
            }
            Spacer(modifier = Modifier.size(20.dp))
            ProductsView()
        }
        Button(
            onClick = {
                device.let {
                    bluetooth.connectToPrinter(context, it!!)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = stringResource(id = R.string.print_ticket))
        }
    }

}

@Composable
private fun ProductsView() {
    val products = Product.products

    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(products) { product ->
            ProductItem(
                name = product.name,
                price = product.price,
                quantity = product.quantity,
                total = product.price * product.quantity
            )
        }
    }
}

@Composable
private fun ProductItem(name: String, price: Double, quantity: Int, total: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "${quantity}x", fontSize = 18.sp)
            Text(text = "$$price", fontSize = 18.sp)
            Text(text = "$$total", fontSize = 18.sp)
        }
    }
}

fun formatTicket(
    name: String,
    address: String,
    numberPhone: String,
    date: String,
    time: String,
    items: List<Product>,
    total: Double
): String {
    val ESC = "\u001B" // ESC code to print commands
    val NEW_LINE = "\n" // new line
    val boldOn = "${ESC}E1" // Activar negritas
    val boldOff = "${ESC}E0" // Desactivar negritas
    val centerAlign = "${ESC}a1" // Texto centrado
    val leftAlign = "${ESC}a0" // Alinear a la izquierda
    val rightAlign = "${ESC}a2" // Alinear a la derecha
    val divider = "------------------------------"

    val header = """
        $centerAlign$boldOn$name$boldOff$NEW_LINE
        $centerAlign$address$NEW_LINE
        $centerAlign Tel: $numberPhone$NEW_LINE
        $date     $time$NEW_LINE
        $divider$NEW_LINE
    """.trimIndent()

    val itemDetails = items.joinToString(separator = NEW_LINE) { item ->
        "$leftAlign${item.name.padEnd(20)}${item.quantity} x ${item.price} = ${item.price * item.quantity}"
    }

    val footer = """
        $NEW_LINE$divider$NEW_LINE
        $rightAlign$boldOn TOTAL: $boldOff$total$NEW_LINE
        Gracias por su compra $NEW_LINE
    """.trimIndent()

    return header +  itemDetails + footer
}