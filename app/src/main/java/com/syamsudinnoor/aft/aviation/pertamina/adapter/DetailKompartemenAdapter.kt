package com.syamsudinnoor.aft.aviation.pertamina.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syamsudinnoor.aft.aviation.pertamina.databinding.HolderTableViewBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.DetailKompartemen
import com.syamsudinnoor.aft.aviation.pertamina.utility.numberFormatter

class DetailKompartemenAdapter(private val detailKomp: List<DetailKompartemen>) : RecyclerView.Adapter<DetailKompartemenAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val holderView = HolderTableViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(holderView)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(detailKomp[position])
    }

    override fun getItemCount(): Int = detailKomp.size

    class ViewHolder(private val binding : HolderTableViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(kompartemen : DetailKompartemen){
            binding.txtTitle.text = "Kompartemen " + kompartemen.kompartemen
            binding.row2Body.text = kompartemen.tera.toString()
            binding.row3Body.text = kompartemen.ukuran_supply_point?.toString() ?: "-"
            binding.row4Body.text = kompartemen.rmm_i.toString()
            binding.row5Body.text = kompartemen.ukuran_dppu.toString()
            binding.row6Body.text = numberFormatter(kompartemen.density_obs ?: 0.0)
            binding.row7Body.text = kompartemen.temp_obs.toString()
            binding.row8Body.text = kompartemen.selisih_ullage.toString()
            binding.row9Body.text = numberFormatter(kompartemen.selisih_liter ?: 0.0)
            binding.row10Body.text = numberFormatter(kompartemen.corr_factor ?: 0.0)
            binding.row11Body.text = numberFormatter(kompartemen.density_15 ?: 0.0)
            binding.row12Body.text = numberFormatter(kompartemen.liter_15 ?: 0.0)
        }

    }
}