package com.gdsc.goldenhour.view.guide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentGuideBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.Guide
import com.gdsc.goldenhour.network.model.GuideList
import com.gdsc.goldenhour.view.guide.adapter.GuideAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuideFragment : BindingFragment<FragmentGuideBinding>(FragmentGuideBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadGuideList()
    }

    private fun loadGuideList() {
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
                val intent = Intent(context, GuideWebtoonActivity::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("name", item.name)
                startActivity(intent)
            }
        })

        recyclerView.adapter = guideAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
    }
}