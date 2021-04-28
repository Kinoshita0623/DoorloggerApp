package jp.panta.doorloggerapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow


sealed class Event{
    object DiscoveryStarted : Event()
    data class DeviceFound(
        val device: BluetoothDevice,
    ) : Event()
    object DiscoveryFinished : Event()
}
class DeviceFoundBroadcastReceiver(
    private val callback: (Event)-> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null || intent == null) {
            return
        }
        when (intent.action) {
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                callback.invoke(Event.DiscoveryStarted)
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                callback.invoke(Event.DiscoveryFinished)
            }
            BluetoothDevice.ACTION_FOUND -> {
                intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)?.let {
                    callback.invoke(Event.DeviceFound(it))
                }
            }
        }
    }

}


@ExperimentalCoroutinesApi
fun Context.registerDeviceFoundBroadcastReceiver() : Flow<Event>{
    val intentFilter = IntentFilter().also {
        it.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        it.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        it.addAction(BluetoothDevice.ACTION_FOUND)
    }
    return channelFlow {
        val callback = DeviceFoundBroadcastReceiver{
            offer(it)
        }
        registerReceiver(callback, intentFilter)

        awaitClose {
            unregisterReceiver(callback)
        }
    }
}