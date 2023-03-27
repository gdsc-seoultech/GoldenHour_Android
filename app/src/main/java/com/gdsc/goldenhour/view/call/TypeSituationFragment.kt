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
import com.gdsc.goldenhour.databinding.FragmentSafetyInformationBinding
import com.gdsc.goldenhour.databinding.FragmentTypeSituationBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.TypeSituationList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TypeSituationFragment : Fragment() {

    lateinit var binding: FragmentTypeSituationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTypeSituationBinding.inflate(inflater, container, false)
        val situationId: Int = arguments?.getInt("situationId")!!
        Log.d("tag", "typeSiv" + situationId)
        loadTypeSituation(situationId)
        return binding.root
    }

    private fun loadTypeSituation(id: Int) {

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        RetrofitObject.networkService.getTypeSituationList(id)
            .enqueue(object : Callback<TypeSituationList> {
                override fun onResponse(call: Call<TypeSituationList>, response: Response<TypeSituationList>) {
                    if (response.isSuccessful) {
                        val dataCount = response.body()?.data?.size ?: 0

                        val gridLayout = GridLayout(context)

                        if(id==8 || id==11 || id ==12 || id==14 || id==15 || id==16 || id==18) {
                            gridLayout.columnCount = 2
                        } else {
                            gridLayout.columnCount = 3
                        }

                        for(i in 0 until dataCount) {
                            val button= Button(context)
                            button.text = response.body()!!.data[i].name
                            button.setOnClickListener {
                                Toast.makeText(context, "${response.body()!!.data[i].name}", Toast.LENGTH_SHORT).show()

                                val detailFragment = DetailFragment()
                                val bundle2 = Bundle()
                                bundle2.putInt("situationId2", arguments?.getInt("situationId")!!)

                                val tmp: String = arguments?.getString("key") + "\n" + "사고 유형 : " + response.body()!!.data[i].name
                                bundle2.putString("key", tmp)
                                bundle2.putString("type", arguments!!.getString("type"))
                                detailFragment.arguments = bundle2

                                Log.d("tag", "typesitu " + tmp)
                                Log.d("tag", "typesitu " + response.body()!!.data[i].id)
                                transaction.replace(R.id.container, detailFragment).commit()
                            }

                            gridLayout.addView(button)
                        }
                        binding.typeLayout.addView(gridLayout)
                    }
                }

                override fun onFailure(call: Call<TypeSituationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }
}