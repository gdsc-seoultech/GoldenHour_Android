package com.gdsc.goldenhour.view.guide

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
    private lateinit var guideName: TextView
    private lateinit var aiAssistName: Array<String>
    private lateinit var aiAssistGuide: Array<String>
    private val PERMISSION_REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideWebtoonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        guideName = binding.tvTitle
        guideName.text = intent.getStringExtra("name")

        aiAssistName = resources.getStringArray(R.array.ai_assist_name)
        aiAssistGuide = resources.getStringArray(R.array.ai_assist_guide)

        // 항목 id에 따라 웹툰 이미지를 띄운다.
        val id = intent.getIntExtra("id", 0)
        loadWebtoonImages(id)

        // 항목 이름에 따라 ai 보조 버튼을 추가한다.
        checkDisasterName(guideName.text.toString())

        // AI 보조 버튼이 보이는 경우에만 클릭 리스너가 작동한다.
        binding.ivAiAssist.setOnClickListener {
            checkCameraPermission()
            showGuideDialog()
        }
    }

    private fun checkDisasterName(name: String) {
        if (aiAssistName.contains(name)) {
            binding.ivAiAssist.visibility = View.VISIBLE
        }
    }

    private fun checkCameraPermission() {
        val status = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA")
        if (status == PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "CAMERA Permission Granted...")
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.CAMERA"),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSION", "CAMERA Permission Granted...")
            } else {
                Log.d("PERMISSION", "CAMERA Permission Denied...")
            }
        }
    }

    private fun showGuideDialog() {
        val bloodItems = mutableListOf<String>()
        for (i in 0..3) {
            bloodItems.add(aiAssistName[i])
        }
        val pressureItems = listOf<String>(aiAssistName[4], aiAssistName[5])

        val guideMessage = when (guideName.text) {
            in bloodItems -> aiAssistGuide[0] // 지혈점
            in pressureItems -> aiAssistGuide[1] // 압박점
            else -> "동상, 골절 안내 가이드"
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("안내 가이드")
            .setMessage(guideMessage)
            .setPositiveButton("확인") { _, _ ->
                // todo: 카메라 앱을 켠다.

            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
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

        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.bd_indicator_active
                    )
                )
            } else {
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