package com.example.printtickets

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.widget.Toast
import java.io.IOException
import java.util.UUID

class BluetoothCustom {
    @SuppressLint("MissingPermission")
    fun connectToPrinter(context: Context, device: BluetoothDevice) {
        val uuid =
            device.uuids?.get(0)?.uuid ?: UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        val socket = device.createRfcommSocketToServiceRecord(uuid)

        try {
            socket.connect()

            val outputStream = socket.outputStream
            val ticketData = """
                Receipt
                ---------------
                Item 1      $10.00
                Item 2      $5.00
                Item 3      $11.00
                Item 4      $47.00
                Item 5      $10.00
                Item 6      $5.00
                Item 7      $11.00
                Item 8      $47.00
                ---------------
                Total       $15.00
                
            """.trimIndent()

            outputStream.write(ticketData.toByteArray())
            outputStream.flush()

            Toast.makeText(context, "Ticket sent successfully!", Toast.LENGTH_SHORT).show()
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to connect or print", Toast.LENGTH_SHORT).show()
        }
    }
}