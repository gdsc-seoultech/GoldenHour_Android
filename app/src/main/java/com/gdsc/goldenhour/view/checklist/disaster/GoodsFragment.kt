package com.gdsc.goldenhour.view.checklist.disaster

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentGoodsBinding
import com.gdsc.goldenhour.view.checklist.disaster.adapter.GoodsCheckAdapter
import com.gdsc.goldenhour.view.checklist.disaster.adapter.model.GoodsCheck

class GoodsFragment : BindingFragment<FragmentGoodsBinding>(FragmentGoodsBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 사용자가 설정한 구호물자 리스트 가져오기
        val data = loadUserGoodsList()
        setRecyclerView(data)
    }

    private fun loadUserGoodsList(): List<GoodsCheck> {
        val goods = listOf(
            GoodsCheck("물", false),
            GoodsCheck("방석", false),
            GoodsCheck("물", false),
            GoodsCheck("물", false),
            GoodsCheck("물", false),
            GoodsCheck("방석", false),
            GoodsCheck("방석", false),
            GoodsCheck("물", false),
            GoodsCheck("방석", false),
            GoodsCheck("물", false),
        )
        return goods
    }

    private fun setRecyclerView(data: List<GoodsCheck>) {
        val recyclerView = binding.goodsRecyclerView
        recyclerView.adapter = GoodsCheckAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, VERTICAL)
        recyclerView.addItemDecoration(decoration)
    }
}