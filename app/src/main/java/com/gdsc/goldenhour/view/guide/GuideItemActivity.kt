package com.gdsc.goldenhour.view.guide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gdsc.goldenhour.databinding.ActivityGuideItemBinding
import com.gdsc.goldenhour.network.model.GuideImageList
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.GuideImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuideItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        binding.guideTitle.text = title

        val id = intent.getIntExtra("id", 0)
        loadData(id)
    }

    private fun loadData(id: Int) {
        RetrofitObject.networkService.getGuideImageList(id)
            .enqueue(object : Callback<GuideImageList> {
                override fun onResponse(
                    call: Call<GuideImageList>,
                    response: Response<GuideImageList>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            val itemId = it.data[0].id
                            val itemUrl = it.data[0].imgUrl
                            Log.d("Retrofit", "success image -> $itemId $itemUrl")

                            // todo: 뷰페이저 어댑터에 웹툰 이미지 적용
                            //setViewPager(it.data)
                        }
                    }
                }

                override fun onFailure(call: Call<GuideImageList>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setViewPager(data: List<GuideImage>) {
        TODO("Not yet implemented")
    }
}