package com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.adapter

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.fragment.QualityControlFragment
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment.DataHolderToppingUpFragment
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.fragment.ToppingUpInputFragment

class ToppingUpSectionPagerAdapter(application : AppCompatActivity) : FragmentStateAdapter(application) {
    override fun createFragment(position: Int): Fragment = when(position)
    {
        0 -> ToppingUpInputFragment()
        else -> DataHolderToppingUpFragment()
    }

    override fun getItemCount(): Int  = 2
}