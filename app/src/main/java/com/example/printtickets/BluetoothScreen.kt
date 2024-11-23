package com.example.printtickets

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Preview(showBackground = true)
@Composable
fun BluetoothScreen() {
    val bluetooth = BluetoothCustom()
    val context = LocalContext.current
    val dataStore = StoreDevice(context)
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val pairedDevices = remember { mutableStateListOf<BluetoothDevice>() }
    val selectedDevice = remember { mutableStateOf<BluetoothDevice?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.title_bluetooth_screen),
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                if (bluetoothAdapter?.isEnabled == true) {
                    pairedDevices.clear()
                    pairedDevices.addAll(bluetoothAdapter.bondedDevices)
                } else
                    Toast.makeText(context, "Please enable Bluetooth", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = stringResource(id = R.string.search_devices))
        }
        Spacer(modifier = Modifier.size(16.dp))
        LazyColumn {
            items(pairedDevices) { device ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { selectedDevice.value = device }
                ) {
                    Text(text = device.name ?: "Unknown Device", modifier = Modifier.weight(1f))
                    Text(text = device.address)
                }
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                selectedDevice.value?.let {  device ->
                    CoroutineScope(Dispatchers.IO).launch {
                        dataStore.saveDevice(device)
                    }
                    //bluetooth.connectToPrinter(context, device)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Connect and Print")
        }
    }
}