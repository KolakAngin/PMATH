package com.syamsudinnoor.aft.aviation.pertamina.ui.density.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.fragment.DataQualityControlFragment
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.quality_control.fragment.QualityControlFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity){
    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when (position) {
            0 -> fragment = QualityControlFragment()
            1 -> fragment = DataQualityControlFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2

}