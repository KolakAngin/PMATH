package com.syamsudinnoor.aft.aviation.pertamina.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.HolderQualityControlBinding
import com.syamsudinnoor.aft.aviation.pertamina.privateDatabase.entity.ToppingUp
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment.KonsinyasiFragment
import com.syamsudinnoor.aft.aviation.pertamina.utility.TimeConverter
import com.syamsudinnoor.aft.aviation.pertamina.utility.formatterNumber

class ToppingUpAdapter(private val onItemClicked: (ToppingUp) -> Unit,
                       private val onItemLongClicked : (ToppingUp) -> Unit):
    ListAdapter<ToppingUp, ToppingUpAdapter.ViewHolder>(ToppingUpCallback()){

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
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClicked(getItem(position))
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClicked(getItem(position))
            true
        }
    }


    class ViewHolder(val binding : HolderQualityControlBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ToppingUp){
            binding.txtBridgerHolder.text = item.snr_no ?: ""
            binding.txtLiterHolder.text = formatterNumber(item.jumlah_topping)
            binding.txtSealHolder.text = item.totalisator_akhir ?: ""
            binding.txtTangki.text = item.tanki ?: ""
            binding.operator.text = item.operator ?: ""
            binding.timeDataQuilty.text = TimeConverter.toReadableDateTime(item.start_time ?: System.currentTimeMillis())

            if (item.status == KonsinyasiFragment.STATUS){
                binding.holderCardViewQualityControl.setBackgroundResource(R.color.warning_2)
            }
        }
    }
}

private class ToppingUpCallback : DiffUtil.ItemCallback<ToppingUp>(){
    override fun areItemsTheSame(oldItem: ToppingUp, newItem: ToppingUp): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ToppingUp, newItem: ToppingUp): Boolean = oldItem == newItem

}