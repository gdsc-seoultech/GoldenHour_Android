package com.gdsc.goldenhour.view.checklist.normal

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentNormalOneBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.Disaster
import com.gdsc.goldenhour.network.model.DisasterList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NormalOneFragment :
    BindingFragment<FragmentNormalOneBinding>(FragmentNormalOneBinding::inflate) {
    lateinit var listView: ListView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = binding.disasterListView

        listView.setOnItemClickListener { _, _, pos, _ ->
            val name = listView.getItemAtPosition(pos).toString()
            loadFragment(DisasterWebtoonFragment(pos + 1, name))
        }

        loadDisasterList()
    }

    private fun loadFragment(fragment: Fragment) {
        val tran = activity?.supportFragmentManager?.beginTransaction()
        tran?.replace(R.id.tab_content, fragment)
        tran?.commit()
    }

    private fun loadDisasterList() {
        RetrofitObject.networkService.getDisasterList()
            .enqueue(object : Callback<DisasterList> {
                override fun onResponse(
                    call: Call<DisasterList>,
                    response: Response<DisasterList>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Retrofit", "success normal one fragment")
                        val body = response.body()
                        body?.let {
                            setListView(it.data)
                        }
                    }
                }

                override fun onFailure(call: Call<DisasterList>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setListView(data: List<Disaster>) {
        val nameList = mutableListOf<String>()
        for(element in data){
            nameList.add(element.name)
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            nameList
        )

        listView.adapter = adapter
    }
}