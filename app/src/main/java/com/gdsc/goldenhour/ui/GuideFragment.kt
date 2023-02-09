package com.gdsc.goldenhour.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.gdsc.goldenhour.GuideItemActivity
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.adapter.GuideAdapter
import com.gdsc.goldenhour.data.Datasource
import com.gdsc.goldenhour.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentGuideBinding.bind(view)

        val guideDataset = Datasource().loadGuideItems()

        val recyclerView = binding.guideRecyclerView
        val guideAdapter = GuideAdapter(context, guideDataset)

        guideAdapter.setMyItemClickListener(object: GuideAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val item = guideDataset[position]
                val title = context?.resources?.getString(item.stringResourceId)

                // GuideItemActivity로 텍스트 데이터를 넘긴다.
                val intent = Intent(context, GuideItemActivity::class.java)
                intent.putExtra("title", title)
                startActivity(intent)
            }
        })

        recyclerView.adapter = guideAdapter

        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager

        recyclerView.setHasFixedSize(true)

        // todo: 각 항목을 클릭하면 타이틀 & 슬라이드 웹툰 페이지



    }
}