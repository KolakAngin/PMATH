package com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.fragment.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.fragment.FragmentDippingRefeuller
import com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.fragment.FragmentDippingTank

class DippingSectionAdapter(application : AppCompatActivity) : FragmentStateAdapter(application) {
    override fun createFragment(position: Int): Fragment = when(position){
        0 -> FragmentDippingTank()
        1 -> FragmentDippingRefeuller()
        else -> FragmentDippingTank()
    }

    override fun getItemCount() : Int = 2
}