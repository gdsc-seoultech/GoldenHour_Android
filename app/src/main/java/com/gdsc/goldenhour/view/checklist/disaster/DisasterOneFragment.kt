package com.gdsc.goldenhour.view.checklist.disaster

import android.os.Bundle
import android.view.View
import com.gdsc.goldenhour.adapter.WebtoonAdapter
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentDisasterOneBinding
import com.gdsc.goldenhour.network.model.WebtoonItem

class DisasterOneFragment :
    BindingFragment<FragmentDisasterOneBinding>(FragmentDisasterOneBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val disasterName = "화재"
        loadWebtoonImages(disasterName)
    }

    private fun loadWebtoonImages(disasterName: String) {
        // TODO: 재난 이름에 따른 행동수칙을 받을 수 있도록 (id 말고)
    }

    private fun setViewPagerRecyclerView(data: List<WebtoonItem>) {
        val viewpager = binding.disasterViewpager
        viewpager.offscreenPageLimit = 1
        viewpager.adapter = WebtoonAdapter(context, data)
    }
}