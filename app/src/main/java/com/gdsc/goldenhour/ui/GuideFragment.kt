package com.gdsc.goldenhour.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
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
        recyclerView.adapter = GuideAdapter(context, guideDataset)

        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager

        recyclerView.setHasFixedSize(true)
    }
}