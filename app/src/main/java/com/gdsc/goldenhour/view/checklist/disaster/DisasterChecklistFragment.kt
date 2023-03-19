package com.gdsc.goldenhour.view.checklist.disaster

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentChecklistBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.DisasterList
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisasterChecklistFragment(
    private val disasterSMS: String,
    private val disasterName: String
) : BindingFragment<FragmentChecklistBinding>(FragmentChecklistBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateDisasterModeView()

        loadFragment(DisasterOneFragment(disasterName))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabNameArray = resources.getStringArray(R.array.checklist_tab_name)
                when(tab?.text){
                    tabNameArray[0] -> loadFragment(DisasterOneFragment(disasterName))
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

    private fun updateDisasterModeView() {
        binding.tvDisasterSms.visibility = View.VISIBLE
        binding.tvDisasterSms.text = "재난문자\n\n${disasterSMS}"

        binding.tabLayout.setTabTextColors(
            resources.getColor(R.color.dark_gray),
            resources.getColor(R.color.red)
        )
        binding.tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.red))
    }

    private fun loadFragment(fragment: Fragment) {
        val tran = requireActivity().supportFragmentManager.beginTransaction()
        tran.replace(R.id.tab_content, fragment)
        tran.commit()
    }
}