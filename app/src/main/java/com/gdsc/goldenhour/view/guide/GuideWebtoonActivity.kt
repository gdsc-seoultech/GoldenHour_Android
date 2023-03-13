package com.gdsc.goldenhour.view.guide

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivityGuideWebtoonBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.WebtoonItem
import com.gdsc.goldenhour.network.model.GuideWebtoonList
import com.gdsc.goldenhour.adapter.WebtoonAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GuideWebtoonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideWebtoonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideWebtoonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        binding.guideTitle.text = title

        checkDisasterName(title)

        val id = intent.getIntExtra("id", 0)
        loadWebtoonImages(id)
    }

    private fun checkDisasterName(title: String?) {
        val aiAssistArray = resources.getStringArray(R.array.ai_assist_array)
        if(aiAssistArray.contains(title)){
            // ai 보조 버튼 보이도록
            binding.aiAssistBtn.visibility = View.VISIBLE
            binding.aiAssistBtn.setOnClickListener {
                Log.d("AI BUTTON", "버튼 클릭")

                // TODO: 넘어간 화면에서 재난 이름을 1~4번 카테고리로 분류하여 그에 따른 AI 기능 제공
                // TODO: 1. 압박점 2. 지혈 3. 동상 4. 골절
            }
        }
    }

    private fun loadWebtoonImages(id: Int) {
        RetrofitObject.networkService.getGuideWebtoonList(id)
            .enqueue(object : Callback<GuideWebtoonList> {
                override fun onResponse(
                    call: Call<GuideWebtoonList>,
                    response: Response<GuideWebtoonList>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            setViewPagerRecyclerView(it.data)
                        }
                    }
                }

                override fun onFailure(call: Call<GuideWebtoonList>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setViewPagerRecyclerView(data: List<WebtoonItem>) {
        val guideViewpager = binding.guideViewpager
        guideViewpager.offscreenPageLimit = 1 // 얼만큼 떨어져 있는 페이지를 미리 생성할 것인지 (1이면 좌우 페이지 미리 생성)
        guideViewpager.adapter = WebtoonAdapter(this, data)

        guideViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
            indicators[i] = ImageView(this)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
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
                        this,
                        R.drawable.bd_indicator_active
                    )
                )
            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.bd_indicator_inactive
                    )
                )
            }
        }
    }
}