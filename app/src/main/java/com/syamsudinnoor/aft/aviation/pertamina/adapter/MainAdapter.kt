package com.syamsudinnoor.aft.aviation.pertamina.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syamsudinnoor.aft.aviation.pertamina.databinding.MainHolderBinding

class MainAdapter(private var menuList: ArrayList<ItemMenu>) : RecyclerView.Adapter<MainAdapter.MenuViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuViewHolder {
        val binding = MainHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    private var onItemClickCallback: setOnItemClickCallback? = null


    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val itemMenu = menuList[position]
        holder.bind(itemMenu)
        holder.itemView.setOnClickListener { onItemClickCallback?.setOnItemClick(itemMenu) }
    }

    override fun getItemCount() = menuList.size

    fun setFilteredList(filteredList: ArrayList<ItemMenu>){
        this.menuList = filteredList
        notifyDataSetChanged()
    }

    fun adapterPassItemCallback(setCallback: setOnItemClickCallback) {
        this.onItemClickCallback = setCallback
    }


    class MenuViewHolder(val binding: MainHolderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(itemMenu: ItemMenu){
            binding.holderImg.setImageResource(itemMenu.icon)
            binding.holderText.text = itemMenu.name
        }
    }
}

fun interface setOnItemClickCallback {

    fun setOnItemClick(itemMenu: ItemMenu)
}