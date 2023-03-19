package com.gdsc.goldenhour.view.checklist.normal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentDisasterWebtoonBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.WebtoonItem
import com.gdsc.goldenhour.adapter.WebtoonAdapter
import com.gdsc.goldenhour.network.model.WebtoonList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisasterWebtoonFragment(
    private val itemId: Int,
    private val itemName: String
) : BindingFragment<FragmentDisasterWebtoonBinding>(FragmentDisasterWebtoonBinding::inflate) {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val tran = requireActivity().supportFragmentManager.beginTransaction()
                tran.replace(R.id.tab_content, NormalOneFragment())
                tran.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.disasterName.text = itemName
        loadWebtoonImages(itemId)
    }

    private fun loadWebtoonImages(id: Int) {
        RetrofitObject.networkService.getDisasterWebtoonListById(id)
            .enqueue(object : Callback<WebtoonList> {
                override fun onResponse(
                    call: Call<WebtoonList>,
                    response: Response<WebtoonList>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Retrofit", "success disaster webtoon fragment")
                        val body = response.body()
                        body?.let {
                            setViewPagerRecyclerView(it.data)
                        }
                    }
                }

                override fun onFailure(call: Call<WebtoonList>, t: Throwable) {
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