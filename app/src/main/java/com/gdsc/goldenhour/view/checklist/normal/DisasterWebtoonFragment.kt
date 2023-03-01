package com.gdsc.goldenhour.view.checklist.normal

import android.os.Bundle
import android.util.Log
import android.view.View
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentDisasterWebtoonBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.DisasterWebtoonList
import com.gdsc.goldenhour.network.model.WebtoonItem
import com.gdsc.goldenhour.adapter.WebtoonAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisasterWebtoonFragment(
    private val itemId: Int,
    private val itemName: String
) : BindingFragment<FragmentDisasterWebtoonBinding>(FragmentDisasterWebtoonBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.disasterName.text = itemName
        loadWebtoonImages(itemId)
    }

    private fun loadWebtoonImages(id: Int) {
        RetrofitObject.networkService.getDisasterWebtoonList(id)
            .enqueue(object : Callback<DisasterWebtoonList> {
                override fun onResponse(
                    call: Call<DisasterWebtoonList>,
                    response: Response<DisasterWebtoonList>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Retrofit", "success disaster webtoon fragment")
                        val body = response.body()
                        body?.let {
                            setViewPagerRecyclerView(it.data)
                        }
                    }
                }

                override fun onFailure(call: Call<DisasterWebtoonList>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setViewPagerRecyclerView(data: List<WebtoonItem>) {
        val viewpager = binding.normalViewpager
        viewpager.offscreenPageLimit = 1
        viewpager.adapter = WebtoonAdapter(context, data)
    }
}