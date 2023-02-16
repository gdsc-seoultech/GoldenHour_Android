package com.gdsc.goldenhour.view.guide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.gdsc.goldenhour.view.guide.adapter.GuideAdapter
import com.gdsc.goldenhour.databinding.FragmentGuideBinding
import com.gdsc.goldenhour.network.model.Guide
import com.gdsc.goldenhour.network.model.GuideList
import com.gdsc.goldenhour.network.RetrofitObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuideFragment : Fragment() {
    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
    }

    private fun loadData() {
        RetrofitObject.networkService.getGuideList()
            .enqueue(object : Callback<GuideList> {
                override fun onResponse(call: Call<GuideList>, response: Response<GuideList>) {
                    if (response.isSuccessful) {
                        Log.d("Retrofit", "success guide fragment")
                        val body = response.body()
                        body?.let {
                            setRecyclerView(it.data)
                        }
                    }
                }

                override fun onFailure(call: Call<GuideList>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setRecyclerView(guideList: List<Guide>) {
        val recyclerView = binding.guideRecyclerView
        val guideAdapter = GuideAdapter(context, guideList)

        guideAdapter.setMyItemClickListener(object : GuideAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = guideList[position]
                val intent = Intent(context, GuideItemActivity::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("title", item.name)
                startActivity(intent)
            }
        })

        recyclerView.adapter = guideAdapter
        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}