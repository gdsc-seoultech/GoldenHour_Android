package com.gdsc.goldenhour.view.checklist.disaster

import android.os.Bundle
import android.util.Log
import android.view.View
import com.gdsc.goldenhour.adapter.WebtoonAdapter
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentDisasterOneBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.WebtoonItem
import com.gdsc.goldenhour.network.model.WebtoonList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisasterOneFragment(
    private val disasterName: String
) :
    BindingFragment<FragmentDisasterOneBinding>(FragmentDisasterOneBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadWebtoonImages(disasterName)
    }

    private fun loadWebtoonImages(disasterName: String) {
        Log.e("Retrofit", disasterName)
        RetrofitObject.networkService.getDisasterWebtoonListByName(disasterName)
            .enqueue(object: Callback<WebtoonList> {
                override fun onResponse(call: Call<WebtoonList>, response: Response<WebtoonList>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            setViewPagerRecyclerView(it.data)
                        }
                    }else{
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<WebtoonList>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setViewPagerRecyclerView(data: List<WebtoonItem>) {
        val viewpager = binding.disasterViewpager
        viewpager.offscreenPageLimit = 1
        viewpager.adapter = WebtoonAdapter(context, data)
    }
}