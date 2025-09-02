package com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.fragment.DataHolderVolumeControlFragment
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.volume_control.fragment.VolumeControlFragment

class SectionQualityControlAdapter(context : AppCompatActivity) : FragmentStateAdapter(context) {
    override fun createFragment(position: Int): Fragment  = when(position){
        0 -> VolumeControlFragment()
        1 -> DataHolderVolumeControlFragment()
        else -> VolumeControlFragment()
    }
    override fun getItemCount(): Int = 2

}