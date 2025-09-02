package com.syamsudinnoor.aft.aviation.pertamina.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.syamsudinnoor.aft.aviation.pertamina.databinding.CardViewHolderBinding
import com.syamsudinnoor.aft.aviation.pertamina.databinding.HolderQualityControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalisaVolumeControlWithDetail
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.BridgerQualityControl
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.formatterNumber
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter


open class VolumeControlDiffCallback : DiffUtil.ItemCallback<AnalisaVolumeControlWithDetail>() {
    override fun areItemsTheSame(
        oldItem: AnalisaVolumeControlWithDetail,
        newItem: AnalisaVolumeControlWithDetail
    ): Boolean = oldItem.analisaVolumeControl.idAnalisa == newItem.analisaVolumeControl.idAnalisa

    override fun areContentsTheSame(
        oldItem: AnalisaVolumeControlWithDetail,
        newItem: AnalisaVolumeControlWithDetail
    ): Boolean = oldItem == newItem

}

class VolumeControlAdapter (private val onClickAction : (AnalisaVolumeControlWithDetail) -> Unit,
                            private val onLongClickAction :(AnalisaVolumeControlWithDetail) -> Unit) :
    ListAdapter<AnalisaVolumeControlWithDetail, VolumeControlAdapter.ViewHolder>(VolumeControlDiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = HolderQualityControlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.bind(item)
        holder.binding.holderCardViewQualityControl.setOnClickListener {
            onClickAction(item)
        }
        holder.binding.holderCardViewQualityControl.setOnLongClickListener {
            onLongClickAction(item)
            true
        }
    }
    class ViewHolder(val binding: HolderQualityControlBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : AnalisaVolumeControlWithDetail){
            binding.txtBridgerHolder.text = item.analisaVolumeControl.no_polisi.toString()
            binding.timeDataQuilty.text = TimeConverter.toReadableDateTime(item.analisaVolumeControl.tanggal ?: System.currentTimeMillis())
            binding.txtLiterHolder.text = formatterNumber(item.analisaVolumeControl.kuantitas)
            binding.txtSealHolder.text = item.analisaVolumeControl.spv_rsd
            binding.operator.text = item.analisaVolumeControl.sopir_bridger_1
            binding.txtTangki.text = item.analisaVolumeControl.sopir_bridger_2


        }
    }
}
