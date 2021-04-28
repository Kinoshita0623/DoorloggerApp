package jp.panta.doorloggerapp

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import jp.panta.doorloggerapp.databinding.ActivityDeviceConnectionBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull

class DeviceConnectionActivity : AppCompatActivity() {
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_connection)

        val binding = DataBindingUtil.setContentView<ActivityDeviceConnectionBinding>(this, R.layout.activity_device_connection)
        binding.deviceTokenView.text = intent.data.toString()
        binding.deviceTokenView.text = intent.data?.getQueryParameter("token")


        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        lifecycleScope.launchWhenStarted {
            this@DeviceConnectionActivity.registerDeviceFoundBroadcastReceiver().mapNotNull {

            }
        }

    }
}