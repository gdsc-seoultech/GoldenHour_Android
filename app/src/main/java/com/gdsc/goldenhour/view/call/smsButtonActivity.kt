package com.gdsc.goldenhour.view.call

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivitySmsButtonBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.*
import com.gdsc.goldenhour.view.guide.GuideWebtoonActivity
import com.gdsc.goldenhour.view.guide.adapter.GuideAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.URL

class smsButtonActivity : AppCompatActivity() {
    lateinit var binding: ActivitySmsButtonBinding
    var message: String = ""

    var situationId: Int = 0
    var typeSituationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn112.setOnClickListener {
            binding.typeLayout.removeAllViews()
            binding.typeSituationLayout.removeAllViews()
            binding.detailSituationLayout.removeAllViews()
            binding.informationLayout.removeAllViews()
            binding.safetyInformationLayout.removeAllViews()
            binding.numLayout.removeAllViews()
            binding.peopleInformationLayout.removeAllViews()
            loadSituation("112")
        }
        binding.btn119.setOnClickListener {
            binding.typeLayout.removeAllViews()
            binding.typeSituationLayout.removeAllViews()
            binding.detailSituationLayout.removeAllViews()
            binding.informationLayout.removeAllViews()
            binding.safetyInformationLayout.removeAllViews()
            binding.numLayout.removeAllViews()
            binding.peopleInformationLayout.removeAllViews()
            loadSituation("119")
        }
        binding.btn110.setOnClickListener {
            binding.typeLayout.removeAllViews()
            binding.typeSituationLayout.removeAllViews()
            binding.detailSituationLayout.removeAllViews()
            binding.informationLayout.removeAllViews()
            binding.safetyInformationLayout.removeAllViews()
            binding.numLayout.removeAllViews()
            binding.peopleInformationLayout.removeAllViews()
        }
    }

    private fun loadSituation(type: String) {
        Log.d("tag", "oncreate")
        Log.d("tag", type)
        RetrofitObject.networkService.getSituationList(type)
            .enqueue(object : Callback<SituationList> {
                override fun onResponse(call: Call<SituationList>, response: Response<SituationList>) {
                    if (response.isSuccessful) {
                        val dataCount = response.body()?.data?.size ?: 0

                        val gridLayout = GridLayout(applicationContext)
                        gridLayout.columnCount = 3

                        for(i in 0 until dataCount) {
                            val button= Button(applicationContext)
                            button.text = response.body()!!.data[i].name
                            button.setOnClickListener {
                                Toast.makeText(applicationContext, "${response.body()!!.data[i].name}, ${response.body()!!.data[i].id}", Toast.LENGTH_SHORT).show()

                                //binding.typeLayout.removeAllViews()
                                binding.typeSituationLayout.removeAllViews()
                                binding.detailSituationLayout.removeAllViews()
                                binding.informationLayout.removeAllViews()
                                binding.safetyInformationLayout.removeAllViews()
                                binding.numLayout.removeAllViews()
                                binding.peopleInformationLayout.removeAllViews()

                                loadTypeSituation(response.body()!!.data[i].id)
                                situationId = response.body()!!.data[i].id
                            }

                            gridLayout.addView(button)
                        }
                        binding.typeLayout.addView(gridLayout)
                    }
                }

                override fun onFailure(call: Call<SituationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun loadTypeSituation(id: Int) {
        RetrofitObject.networkService.getTypeSituationList(id)
            .enqueue(object : Callback<TypeSituationList> {
                override fun onResponse(call: Call<TypeSituationList>, response: Response<TypeSituationList>) {
                    if (response.isSuccessful) {
                        val dataCount = response.body()?.data?.size ?: 0

                        val gridLayout = GridLayout(applicationContext)

                        if(id==8 || id==11 || id ==12 || id==14 || id==15 || id==16 || id==18) {
                            gridLayout.columnCount = 2
                        } else {
                            gridLayout.columnCount = 3
                        }

                        for(i in 0 until dataCount) {
                            val button= Button(applicationContext)
                            button.text = response.body()!!.data[i].name
                            button.setOnClickListener {
                                Toast.makeText(applicationContext, "${response.body()!!.data[i].name}, ${response.body()!!.data[i].id}", Toast.LENGTH_SHORT).show()

                                //binding.typeSituationLayout.removeAllViews()
                                binding.detailSituationLayout.removeAllViews()
                                binding.informationLayout.removeAllViews()
                                binding.safetyInformationLayout.removeAllViews()
                                binding.numLayout.removeAllViews()
                                binding.peopleInformationLayout.removeAllViews()

                                loadDetailSituation(situationId)
                                typeSituationId = response.body()!!.data[i].id
                            }

                            gridLayout.addView(button)
                        }
                        binding.typeSituationLayout.addView(gridLayout)
                    }
                }

                override fun onFailure(call: Call<TypeSituationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun loadDetailSituation(id: Int) {
        RetrofitObject.networkService.getDetailSituationList(id)
            .enqueue(object : Callback<DetailSituationList> {
                override fun onResponse(call: Call<DetailSituationList>, response: Response<DetailSituationList>) {
                    if (response.isSuccessful) {
                        val dataCount = response.body()?.data?.size ?: 0

                        val gridLayout = GridLayout(applicationContext)
                        gridLayout.columnCount = 1


                        for(i in 0 until dataCount) {
                            val button= Button(applicationContext)
                            button.text = response.body()!!.data[i].name
                            button.setOnClickListener {
                                Toast.makeText(applicationContext, "${response.body()!!.data[i].name}, ${response.body()!!.data[i].id}", Toast.LENGTH_SHORT).show()

                                binding.informationLayout.removeAllViews()
                                binding.safetyInformationLayout.removeAllViews()
                                binding.numLayout.removeAllViews()
                                binding.peopleInformationLayout.removeAllViews()

                                loadInformation(situationId)
                            }

                            gridLayout.addView(button)
                        }
                        binding.detailSituationLayout.addView(gridLayout)
                    }
                }

                override fun onFailure(call: Call<DetailSituationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun loadInformation(id: Int) {
        RetrofitObject.networkService.getInformationList(id)
            .enqueue(object : Callback<InformationList> {
                override fun onResponse(call: Call<InformationList>, response: Response<InformationList>) {
                    if (response.isSuccessful) {
                        val dataCount = response.body()?.data?.size ?: 0

                        val gridLayout = GridLayout(applicationContext)
                        gridLayout.columnCount = 3


                        for(i in 0 until dataCount) {
                            for(j in 0 until (response.body()?.data?.get(i)?.answerList?.size ?: 0)) {
                                val button= Button(applicationContext)
                                button.text = response.body()!!.data[i].answerList[j]
                                button.setOnClickListener {
                                    binding.safetyInformationLayout.removeAllViews()
                                    binding.numLayout.removeAllViews()
                                    binding.peopleInformationLayout.removeAllViews()

                                    Toast.makeText(applicationContext, "${response.body()!!.data[i].answerList[j]}, ${response.body()!!.data[i].id}", Toast.LENGTH_SHORT).show()
                                    loadSafetyInformation()
                                //binding.informationLayout.removeAllViews()
                                }
                                gridLayout.addView(button)
                            }


                        }
                        binding.informationLayout.addView(gridLayout)
                    }
                }

                override fun onFailure(call: Call<InformationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun loadSafetyInformation() {
        val gridLayout = GridLayout(applicationContext)
        gridLayout.columnCount = 2

        val array: Array<String> = arrayOf("안전 지역에 있다", "사고 현장에 있다", "사고로 부상을 당했다", "사고로 움직이지 못한다")

        for(i in 0 until 4) {
            val button= Button(applicationContext)
            button.text = array[i]
            button.setOnClickListener {
                binding.numLayout.removeAllViews()
                binding.peopleInformationLayout.removeAllViews()

                Toast.makeText(applicationContext, "${array[i]}", Toast.LENGTH_SHORT).show()
                loadNum()
            }
            gridLayout.addView(button)

        }
        binding.safetyInformationLayout.addView(gridLayout)
    }

    private fun loadNum() {
        val gridLayout = GridLayout(applicationContext)
        gridLayout.columnCount = 4

        val array: Array<String> = arrayOf("알 수 없다", "1명", "2명", "3명 이상")

        for(i in 0 until 4) {
            val button= Button(applicationContext)
            button.text = array[i]
            button.setOnClickListener {
                binding.peopleInformationLayout.removeAllViews()

                Toast.makeText(applicationContext, "${array[i]}", Toast.LENGTH_SHORT).show()

                loadPeopleInfo()

            }
            gridLayout.addView(button)

        }
        binding.numLayout.addView(gridLayout)
    }

    private fun loadPeopleInfo() {
        val gridLayout = GridLayout(applicationContext)
        gridLayout.columnCount = 4

        val array: Array<String> = arrayOf("어린이", "임산부", "장애인", "노인", "청소년", "외국인", "남자", "여자")

        for(i in 0 until 8) {
            val button= Button(applicationContext)
            button.text = array[i]
            button.setOnClickListener {
                binding.peopleInformationLayout.removeAllViews()

                Toast.makeText(applicationContext, "${array[i]}", Toast.LENGTH_SHORT).show()

            }
            gridLayout.addView(button)

        }
        binding.peopleInformationLayout.addView(gridLayout)
    }



}