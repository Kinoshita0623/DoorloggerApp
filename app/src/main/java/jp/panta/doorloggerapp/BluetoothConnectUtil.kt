package jp.panta.doorloggerapp

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.util.*

sealed class BLEGattEvent{
    data class ConnectionStateChange(val gatt: BluetoothGatt?, val status: Int, val newState: Int) : BLEGattEvent()
}
@ExperimentalCoroutinesApi
fun BluetoothDevice.connect(context: Context): Flow<BLEGattEvent> {

    return channelFlow {
        val callback = BluetoothGatCallbackImpl(this)
        val gatt = this@connect.connectGatt(context, false, callback)
        awaitClose{
            gatt.disconnect()
        }
    }
}

class BluetoothGatCallbackImpl @ExperimentalCoroutinesApi constructor(
    private val producerScope: ProducerScope<BLEGattEvent>
) : BluetoothGattCallback() {

    @ExperimentalCoroutinesApi
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        val e = BLEGattEvent.ConnectionStateChange(gatt, status, newState)
        producerScope.offer(e)
    }


    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        super.onCharacteristicChanged(gatt, characteristic)
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        super.onCharacteristicRead(gatt, characteristic, status)

        characteristic?.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
    }

}