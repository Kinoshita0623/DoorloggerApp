package jp.panta.doorloggerapp

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import jp.panta.doorloggerapp.databinding.ActivityDeviceConnectionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import java.util.*

class DeviceConnectionActivity : AppCompatActivity() {

    lateinit var binding: ActivityDeviceConnectionBinding

    var scanDeviceJob: Job? = null
    lateinit var mDeviceListViewModel: DeviceListViewModel

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityDeviceConnectionBinding>(this, R.layout.activity_device_connection)
        binding.deviceTokenView.text = intent.data.toString()
        binding.deviceTokenView.text = intent.data?.getQueryParameter("token")

        mDeviceListViewModel = ViewModelProvider(this, DeviceListViewModel.Factory())[DeviceListViewModel::class.java]


        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        bluetoothAdapter.startScan()
        val adapter = DeviceListAdapter()
        bluetoothAdapter.stopScan()
        adapter.listenDeviceClick().flatMapLatest {
            it.connectRfcommSocket(UUID.fromString("038998d6-680b-3a9b-a4a7-776432addafa"))
                    .read(ByteArray(1024))
        }.onEach {

        }.launchIn(lifecycleScope + Dispatchers.IO)


    }
    @ExperimentalCoroutinesApi
    fun BluetoothAdapter.startScan(): Boolean {
        if(scanDeviceJob?.isActive == false) {
            scanDeviceJob = lifecycleScope.launchWhenStarted {
                this@DeviceConnectionActivity.registerDeviceFoundBroadcastReceiver().mapNotNull {
                    it as? Event.DeviceFound
                }.collect {
                    mDeviceListViewModel.addDevice(it.device)
                }
            }
            return this.startDiscovery()
        }
        return false
    }

    fun BluetoothAdapter.stopScan() {
        if(scanDeviceJob?.isActive == true) {
            scanDeviceJob?.cancel()
            scanDeviceJob = null
            this.cancelDiscovery()
        }
    }
}