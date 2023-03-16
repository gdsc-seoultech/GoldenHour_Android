package com.gdsc.goldenhour.view.checklist.normal

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentNormalTwoBinding

// ArrayAdapter
class NormalTwoFragment :
    BindingFragment<FragmentNormalTwoBinding>(FragmentNormalTwoBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = listOf("물", "방석")
        binding.lvEmergencyGoods.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
    }
}