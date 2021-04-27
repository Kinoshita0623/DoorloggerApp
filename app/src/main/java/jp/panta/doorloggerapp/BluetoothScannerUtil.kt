package jp.panta.doorloggerapp

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.IllegalStateException
import android.bluetooth.le.ScanCallback as AScanCallback


data class OnScanResult(val callbackType: Int, val result: ScanResult?)
class ScanFailedException(val code: Int) : IllegalStateException()
@ExperimentalCoroutinesApi
fun BluetoothLeScanner.scan(): Flow<OnScanResult> {
    return callbackFlow {
        val callback = ScanCallback({ type, result ->
            offer(OnScanResult(type, result))
        }, {
            throw ScanFailedException(it)
        })
        this@scan.startScan(callback)
        awaitClose{
            this@scan.stopScan(callback)

        }
    }
}

class ScanCallback(
    private val onScanResult: (Int, ScanResult?)-> Unit,
    private val onScanFailed: (Int)-> Unit
) : AScanCallback() {

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        onScanResult.invoke(callbackType, result)
    }
    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        onScanFailed.invoke(errorCode)
    }

}