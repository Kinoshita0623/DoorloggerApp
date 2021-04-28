package jp.panta.doorloggerapp

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.panta.doorloggerapp.databinding.ItemDeviceBinding
import androidx.recyclerview.widget.DiffUtil.ItemCallback

class DeviceListAdapter : ListAdapter<BluetoothDevice, DeviceListAdapter.ViewHolder>(DiffItemCallback){

    inner class ViewHolder(val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(device: BluetoothDevice) {
            binding.device = device
            binding.executePendingBindings()
        }
    }

    object DiffItemCallback : ItemCallback<BluetoothDevice>(){
        override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem.address == newItem.address
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate<ItemDeviceBinding>(LayoutInflater.from(parent.context), R.layout.item_device, parent, false))
    }
}