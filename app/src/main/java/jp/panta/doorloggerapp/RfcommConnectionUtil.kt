
package jp.panta.doorloggerapp

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import java.util.*

@Suppress("BlockingMethodInNonBlockingContext")
fun BluetoothDevice.connectRfcommSocket(uuid: UUID): RfcommConnection {
    val socket = this.createRfcommSocketToServiceRecord(uuid)
    socket.connect()
    return RfcommConnection(socket)
}


class RfcommConnection(val bluetoothSocket: BluetoothSocket)

@ExperimentalCoroutinesApi
@Suppress("BlockingMethodInNonBlockingContext")
fun RfcommConnection.read(buffer: ByteArray) : Flow<String>{

    return channelFlow {
        val inputStream = bluetoothSocket.inputStream
        while(bluetoothSocket.isConnected && isActive) {
            val bytes = inputStream.read(buffer)
            offer(String(buffer, 0, bytes))
        }
        awaitClose {
            bluetoothSocket.close()
        }
    }

}