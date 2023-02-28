package com.gdsc.goldenhour.view.checklist.disaster

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentOneBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.DisasterWebtoon
import com.gdsc.goldenhour.network.model.DisasterWebtoonList
import com.gdsc.goldenhour.view.checklist.disaster.adapter.DisasterWebtoonAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OneFragment : BindingFragment<FragmentOneBinding>(FragmentOneBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val disasterName = "화재"
        loadWebtoonImages(disasterName)
    }

    private fun loadWebtoonImages(disasterName: String) {
        // TODO: 재난 이름에 따른 행동수칙을 받을 수 있도록 (id 말고)
        // TODO: getDisasterWebtoonList 이 함수는 임의로 만들어둔 것!
        RetrofitObject.networkService.getDisasterWebtoonList(disasterName)
            .enqueue(object : Callback<DisasterWebtoonList> {
                override fun onResponse(
                    call: Call<DisasterWebtoonList>,
                    response: Response<DisasterWebtoonList>
                ) {
                    if (response.isSuccessful) {
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

    private fun setViewPagerRecyclerView(data: List<DisasterWebtoon>) {
        val disasterViewpager = binding.checklistDisasterViewpager
        disasterViewpager.offscreenPageLimit = 1
        disasterViewpager.adapter = DisasterWebtoonAdapter(context, data)

        disasterViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        setUpIndicators(data.size)
    }

    private fun setUpIndicators(size: Int) {
        val indicators = arrayOfNulls<ImageView>(size)

        // indicator 레이아웃 설정하기
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 10, 16, 10)

        // LinearLayout에 동적으로 indicator 추가하기
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bd_indicator_inactive
                )
            )
            indicators[i]?.layoutParams = params
            binding.indicatorContainer.addView(indicators[i])
        }

        setCurrentIndicator(0)
    }
    private fun setCurrentIndicator(position: Int) {
        val indicatorContainer = binding.indicatorContainer
        val childCount = indicatorContainer.childCount

        for(i in 0 until childCount){
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if(i == position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bd_indicator_active
                    )
                )
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bd_indicator_inactive
                    )
                )
            }
        }
    }
}