package jp.panta.doorloggerapp

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import jp.panta.doorloggerapp.databinding.ActivityDeviceConnectionBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull

class DeviceConnectionActivity : AppCompatActivity() {

    lateinit var binding: ActivityDeviceConnectionBinding

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityDeviceConnectionBinding>(this, R.layout.activity_device_connection)
        binding.deviceTokenView.text = intent.data.toString()
        binding.deviceTokenView.text = intent.data?.getQueryParameter("token")

        val deviceListViewModel = ViewModelProvider(this, DeviceListViewModel.Factory())[DeviceListViewModel::class.java]

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        lifecycleScope.launchWhenStarted {
            this@DeviceConnectionActivity.registerDeviceFoundBroadcastReceiver().mapNotNull {
                it as? Event.DeviceFound
            }.collect {
                deviceListViewModel.addDevice(it.device)
            }
        }

    }
}