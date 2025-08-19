package com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.fragment.QualityControlFragment
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.fragment.VolumeControlFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity){
    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when (position) {
            0 -> fragment = QualityControlFragment()
            1 -> fragment = VolumeControlFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2

}