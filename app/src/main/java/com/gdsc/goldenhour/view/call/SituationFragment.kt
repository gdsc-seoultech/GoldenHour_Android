package com.gdsc.goldenhour.view.call

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
import com.gdsc.goldenhour.databinding.FragmentSituationBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.SituationList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SituationFragment : Fragment() {

    lateinit var binding: FragmentSituationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSituationBinding.inflate(inflater, container, false)

        val type: String? = arguments?.getString("type")
        loadSituation(type?: "112")

        return binding.root
    }


    private fun loadSituation(type: String) {
        val fragmentManager = requireActivity().supportFragmentManager

        RetrofitObject.networkService.getSituationList(type)
            .enqueue(object : Callback<SituationList> {
                override fun onResponse(call: Call<SituationList>, response: Response<SituationList>) {
                    if (response.isSuccessful) {
                        val dataCount = response.body()?.data?.size ?: 0

                        val gridLayout = GridLayout(context)
                        gridLayout.columnCount = 3

                        for(i in 0 until dataCount) {
                            val button= Button(context)
                            button.text = response.body()!!.data[i].name
                            button.setOnClickListener {
                                Toast.makeText(context, "${response.body()!!.data[i].name}", Toast.LENGTH_SHORT).show()
                                if(response.body()!!.data[i].id >=23) {
                                    if(response.body()!!.data[i].id == 24) {

                                        val btn110Fragment = btn110Fragment()
                                        val bundle2 = Bundle()
                                        val tmp: String = arguments?.getString("key") + "\n" + "상황 : " + response.body()!!.data[i].name
                                        bundle2.putString("key", tmp)
                                        bundle2.putString("type", arguments!!.getString("type"))
                                        btn110Fragment.arguments = bundle2
                                        fragmentManager.beginTransaction().replace(R.id.container, btn110Fragment)
                                            .commit()

                                    } else if(response.body()!!.data[i].id == 26) {

                                        val informationFragment = InformationFragment()
                                        val bundle3 = Bundle()
                                        bundle3.putInt("situationId3", response.body()!!.data[i].id)

                                        val tmp: String = arguments?.getString("key") + "\n" + "상황 : " + response.body()!!.data[i].name
                                        bundle3.putString("key", tmp)
                                        bundle3.putString("type", arguments!!.getString("type"))
                                        informationFragment.arguments = bundle3

                                        fragmentManager.beginTransaction().replace(R.id.container, informationFragment)
                                            .commit()

                                    } else {

                                        val detailFragment = DetailFragment()
                                        val bundle2 = Bundle()
                                        bundle2.putInt("situationId2", response.body()!!.data[i].id)

                                        val tmp: String = arguments?.getString("key") + "\n" + "상황 : " + response.body()!!.data[i].name
                                        bundle2.putString("key", tmp)
                                        bundle2.putString("type", arguments!!.getString("type"))
                                        detailFragment.arguments = bundle2

                                        fragmentManager.beginTransaction().replace(R.id.container, detailFragment)
                                            .commit()

                                    }

                                } else {
                                    val typeSituationFragment = TypeSituationFragment()
                                    val bundle = Bundle()
                                    bundle.putInt("situationId", response.body()!!.data[i].id)

                                    val tmp: String = arguments?.getString("key") + "\n" + "상황 : " + response.body()!!.data[i].name
                                    bundle.putString("key", tmp)
                                    bundle.putString("type", arguments!!.getString("type"))
                                    typeSituationFragment.arguments = bundle
                                    fragmentManager.beginTransaction().replace(R.id.container, typeSituationFragment)
                                        .commit()
                                }

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

}