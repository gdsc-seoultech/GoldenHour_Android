package com.gdsc.goldenhour.view.checklist.disaster

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.DisasterGoodsItemBinding
import com.gdsc.goldenhour.databinding.FragmentDisasterTwoBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.Goods
import com.gdsc.goldenhour.network.model.GoodsReadResponse
import com.gdsc.goldenhour.view.checklist.disaster.adapter.GoodsCheckAdapter
import com.gdsc.goldenhour.view.checklist.disaster.adapter.model.GoodsCheck
import com.google.android.gms.auth.api.signin.GoogleSignIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisasterTwoFragment :
    BindingFragment<FragmentDisasterTwoBinding>(FragmentDisasterTwoBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserGoodsData()
    }

    private fun loadUserGoodsData() {
        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())
        val userIdToken = gsa?.idToken.toString()
        RetrofitObject.networkService.readReliefGoods(userIdToken)
            .enqueue(object : Callback<GoodsReadResponse> {
                override fun onResponse(
                    call: Call<GoodsReadResponse>,
                    response: Response<GoodsReadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            setRecyclerView(responseBody.data)
                        }
                        Log.d("Retrofit", "success GET goods list...")
                    } else {
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<GoodsReadResponse>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setRecyclerView(goodsList: List<Goods>) {
        val goodsCheckList = mutableListOf<GoodsCheck>()
        for (goods in goodsList) {
            val item = GoodsCheck(goods.name, false)
            goodsCheckList.add(item)
        }

        val recyclerView = binding.rvGoodsCheck
        recyclerView.adapter = GoodsCheckAdapter(requireContext(), goodsCheckList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
    }
}