package com.gdsc.goldenhour.view.checklist.disaster

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentChecklistBinding
import com.google.android.material.tabs.TabLayout

class ChecklistFragment : BindingFragment<FragmentChecklistBinding>(FragmentChecklistBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFragment(DisasterOneFragment())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabNameArray = resources.getStringArray(R.array.checklist_tab_name)
                when(tab?.text){
                    tabNameArray[0] -> loadFragment(DisasterOneFragment())
                    tabNameArray[1] -> loadFragment(DisasterTwoFragment())
                    tabNameArray[2] -> loadFragment(DisasterThreeFragment())
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun loadFragment(fragment: Fragment) {
        val tran = requireActivity().supportFragmentManager.beginTransaction()
        tran.replace(R.id.tab_content, fragment)
        tran.commit()
    }
}