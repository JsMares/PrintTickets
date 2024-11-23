package com.example.printtickets

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreDevice(private val context: Context) {
    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("storeDevice")
        val STORE_DEVICE = stringPreferencesKey("store_device")
    }

    val getDevice: Flow<BluetoothDevice?> = context.dataStore.data
        .map { preferences ->
            val device = preferences[STORE_DEVICE] ?: ""
            if (device.isNotEmpty())
                BluetoothAdapter.getDefaultAdapter()?.getRemoteDevice(device)
            else
                null
        }

    @SuppressLint("MissingPermission")
    suspend fun saveDevice(device: BluetoothDevice) {
        context.dataStore.edit { preference ->
            preference[STORE_DEVICE] = device.address
        }
    }
}