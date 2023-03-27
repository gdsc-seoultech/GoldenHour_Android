package com.gdsc.goldenhour.view.call

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.FragmentDetailBinding
import com.gdsc.goldenhour.databinding.FragmentSafetyInformationBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.DetailSituationList
import com.gdsc.goldenhour.network.model.Information
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val situationId: Int = arguments?.getInt("situationId2")!!
        loadDetailSituation(situationId)
        return binding.root
    }


    private fun loadDetailSituation(id: Int) {

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        RetrofitObject.networkService.getDetailSituationList(id)
            .enqueue(object : Callback<DetailSituationList> {
                override fun onResponse(call: Call<DetailSituationList>, response: Response<DetailSituationList>) {
                    if (response.isSuccessful) {
                        val dataCount = response.body()?.data?.size ?: 0

                        val gridLayout = GridLayout(context)
                        gridLayout.columnCount = 1


                        for(i in 0 until dataCount) {
                            val button= Button(context)
                            button.text = response.body()!!.data[i].name
                            button.setOnClickListener {
                                Toast.makeText(context, "${response.body()!!.data[i].name}", Toast.LENGTH_SHORT).show()

                                val informationFragment = InformationFragment()
                                val bundle3 = Bundle()
                                bundle3.putInt("situationId3", arguments?.getInt("situationId2")!!)


                                val tmp: String = arguments?.getString("key") + "\n" + "피해상황 : " + response.body()!!.data[i].name
                                bundle3.putString("key", tmp)
                                bundle3.putString("type", arguments!!.getString("type"))
                                informationFragment.arguments = bundle3
                                Log.d("tag", "detail " + tmp)
                                Log.d("tag", "detail " + response.body()!!.data[i].id)
                                transaction.replace(R.id.container, informationFragment).commit()
                            }

                            gridLayout.addView(button)
                        }
                        binding.layout.addView(gridLayout)
                    }
                }

                override fun onFailure(call: Call<DetailSituationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }
}