package com.gdsc.goldenhour.view.checklist.normal

import android.os.Bundle
import android.view.View
import android.widget.SimpleAdapter
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentNormalThreeBinding
import com.gdsc.goldenhour.view.checklist.normal.data.DataSource

// SimpleAdapter
class NormalThreeFragment :
    BindingFragment<FragmentNormalThreeBinding>(FragmentNormalThreeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = DataSource().loadEmergencyContacts()
        binding.lvEmergencyContact.adapter = SimpleAdapter(
            requireContext(),
            data,
            android.R.layout.simple_list_item_2,
            arrayOf("name", "phoneNumber"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
    }
}