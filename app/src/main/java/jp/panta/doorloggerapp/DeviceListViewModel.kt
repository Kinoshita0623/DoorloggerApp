package jp.panta.doorloggerapp

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeviceListViewModel : ViewModel(){


    @Suppress("UNCHECKED_CAST")
    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DeviceListViewModel() as T
        }
    }

    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices: StateFlow<List<BluetoothDevice>> = _devices


    fun addDevices(list: List<BluetoothDevice>) {
        val ex = _devices.value
        val new = list.filter { i ->
            !ex.any { j->
                i.address == j.address
            }
        }
        _devices.value = ex.toMutableList().also {
            it.addAll(new)
        }
    }

    fun addDevice(device: BluetoothDevice) {
        val ex = _devices.value
        val has = ex.any {
            it.address == device.address
        }
        if(has) {
            return
        }
        _devices.value = ex.toMutableList().apply {
            add(device)
        }
    }


}