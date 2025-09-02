package com.syamsudinnoor.aft.aviation.pertamina.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.syamsudinnoor.aft.aviation.pertamina.databinding.HolderQualityControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class BridgerQualityAdapter(
    private val clickListener: (BridgerQualityControl) -> Unit,
    private val longClickListener: (BridgerQualityControl) -> Unit
) : ListAdapter<BridgerQualityControl, BridgerQualityAdapter.ViewHolder>(BridgerQualityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HolderQualityControlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)

        holder.bind(item)
        holder.binding.holderCardViewQualityControl.setOnClickListener {
            clickListener(item)
        }
        holder.binding.holderCardViewQualityControl.setOnLongClickListener {
            longClickListener(item)
            true
        }
    }

    class ViewHolder(val binding: HolderQualityControlBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BridgerQualityControl) {
            binding.txtBridgerHolder.text = item.bridger_no?.uppercase()
            binding.txtLiterHolder.text = item.volume_liter?.toInt().toString()
            binding.txtSealHolder.text = item.seal?.uppercase()
            binding.operator.text = item.operator_name?.uppercase()
            binding.timeDataQuilty.text = TimeConverter.toReadableDateTime(item.dateTime!!)
            binding.txtTangki.text = item.tangki?.uppercase()
        }
    }


    private class BridgerQualityDiffCallback : DiffUtil.ItemCallback<BridgerQualityControl>() {
        override fun areItemsTheSame(oldItem: BridgerQualityControl, newItem: BridgerQualityControl): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BridgerQualityControl, newItem: BridgerQualityControl): Boolean {
            return oldItem == newItem
        }
    }
}