package com.syamsudinnoor.aft.aviation.pertamina.ui.stopwatch.adapter

import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syamsudinnoor.aft.aviation.pertamina.databinding.StopwatchLapItemLayoutBinding

class LapAdapter(private val laps : List<String>) : RecyclerView.Adapter<LapAdapter.LapViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LapViewHolder {
        val binding = StopwatchLapItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LapViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LapViewHolder,
        position: Int
    ) {
        holder.bind(laps[position], position,laps)
    }

    override fun getItemCount(): Int = laps.size

    class LapViewHolder(private val binding : StopwatchLapItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lap : String,position : Int,laps : List<String>){
            binding.tvLapNumber.text = String.format("%02d", laps.size - position)
            binding.tvLapTime.text = lap
        }
    }

}