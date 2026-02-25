package com.syamsudinnoor.aft.aviation.pertamina.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.AnalyticsLayoutBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.AnalyticsDataVolumeControl
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter


open class DiffCallbackSumOfVolumeControl : DiffUtil.ItemCallback<AnalyticsDataVolumeControl>(){
    override fun areItemsTheSame(
        oldItem: AnalyticsDataVolumeControl,
        newItem: AnalyticsDataVolumeControl
    ): Boolean {
        return oldItem.Tanggal == newItem.Tanggal
    }

    override fun areContentsTheSame(
        oldItem: AnalyticsDataVolumeControl,
        newItem: AnalyticsDataVolumeControl
    ): Boolean {
        return oldItem == newItem
    }

}


class AnalyitcsVolumeControlAdapter() :
    ListAdapter<AnalyticsDataVolumeControl, AnalyitcsVolumeControlAdapter.SumOfVolumeControlViewHolder>(DiffCallbackSumOfVolumeControl()){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SumOfVolumeControlViewHolder {
        val binding = AnalyticsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SumOfVolumeControlViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SumOfVolumeControlViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


    class SumOfVolumeControlViewHolder(private val binding: AnalyticsLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: AnalyticsDataVolumeControl){
            binding.txtTanggal.text = TimeConverter.toReadableDate(item.Tanggal)
            binding.txtJumlahBridger.text = item.Total_Bridger.toString()
            binding.txtPlusTotal.text = numberFormatter(item.Selisih)

            if (item.Selisih >= 0){
                binding.imgPlusTotal.setImageResource(R.drawable.up_arrow)
            }else{
                binding.imgPlusTotal.setImageResource(R.drawable.down_arrow)
            }
        }
    }
}