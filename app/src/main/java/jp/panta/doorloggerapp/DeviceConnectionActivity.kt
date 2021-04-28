package jp.panta.doorloggerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import jp.panta.doorloggerapp.databinding.ActivityDeviceConnectionBinding

class DeviceConnectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_connection)

        val binding = DataBindingUtil.setContentView<ActivityDeviceConnectionBinding>(this, R.layout.activity_device_connection)
        binding.deviceTokenView.text = intent.data.toString()
        binding.deviceTokenView.text = intent.data?.getQueryParameter("token")


    }
}