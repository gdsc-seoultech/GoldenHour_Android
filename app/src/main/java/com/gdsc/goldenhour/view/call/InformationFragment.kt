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
import com.gdsc.goldenhour.databinding.FragmentInformationBinding
import com.gdsc.goldenhour.databinding.FragmentSafetyInformationBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.InformationList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InformationFragment : Fragment() {

    lateinit var binding: FragmentInformationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        val situationId: Int = arguments?.getInt("situationId3")!!
        loadInformation(situationId)
        return binding.root
    }


    private fun loadInformation(id: Int) {

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        RetrofitObject.networkService.getInformationList(id)
            .enqueue(object : Callback<InformationList> {
                override fun onResponse(call: Call<InformationList>, response: Response<InformationList>) {
                    if (response.isSuccessful) {
                        val dataCount = response.body()?.data?.size ?: 0

                        val gridLayout = GridLayout(context)
                        gridLayout.columnCount = 3


                        for(i in 0 until dataCount) {
                            for(j in 0 until (response.body()?.data?.get(i)?.answerList?.size ?: 0)) {
                                val button= Button(context)
                                button.text = response.body()!!.data[i].answerList[j]
                                button.setOnClickListener {
                                    Toast.makeText(context, "${response.body()!!.data[i].answerList[j]}", Toast.LENGTH_SHORT).show()

                                    if(arguments?.getInt("situationId3")!! >= 25) {
                                        val btn110Fragment = btn110Fragment()
                                        val totalBundle = Bundle()
                                        val tmp: String = arguments?.getString("key") + "\n" + "피해 상황 : " + response.body()!!.data.get(i).question + " : " + response.body()!!.data[i].answerList[j]
                                        totalBundle.putString("key", tmp)
                                        totalBundle.putString("type", arguments!!.getString("type"))
                                        btn110Fragment.arguments = totalBundle
                                        fragmentManager.beginTransaction().replace(R.id.container, btn110Fragment)
                                            .commit()
                                    } else {
                                        val safetyInformationFragment = SafetyInformationFragment()
                                        val totalBundle = Bundle()
                                        val tmp: String = arguments?.getString("key") + "\n" + response.body()!!.data.get(i).question + " : " + response.body()!!.data[i].answerList[j]
                                        totalBundle.putString("key", tmp)
                                        totalBundle.putString("type", arguments!!.getString("type"))
                                        safetyInformationFragment.arguments = totalBundle

                                        transaction.replace(R.id.container, safetyInformationFragment).commit()
                                    }
                                }
                                gridLayout.addView(button)
                            }
                        }
                        binding.layout.addView(gridLayout)
                    }
                }

                override fun onFailure(call: Call<InformationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }
}