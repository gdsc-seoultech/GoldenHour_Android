package com.gdsc.goldenhour.view.checklist.normal

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.DialogInputFormBinding
import com.gdsc.goldenhour.databinding.FragmentNormalTwoBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NormalTwoFragment :
    BindingFragment<FragmentNormalTwoBinding>(FragmentNormalTwoBinding::inflate) {
    private lateinit var goodsNames: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var userIdToken: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserIdToken()
        getUserGoods()

        binding.fabReliefGoods.setOnClickListener {
            showInputDialog()
        }

        binding.lvEmergencyGoods.setOnItemClickListener { adapterView, view, pos, l ->
            showModifyDialog(pos)
        }

        binding.lvEmergencyGoods.setOnItemLongClickListener { adapterView, view, pos, l ->
            showDeleteDialog(pos)
        }
    }

    private fun initUserIdToken() {
        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())
        userIdToken = gsa?.idToken.toString()
    }

    private fun getUserGoods() {
        RetrofitObject.networkService.readReliefGoods(userIdToken)
            .enqueue(object : Callback<GoodsGetResponse> {
                override fun onResponse(
                    call: Call<GoodsGetResponse>,
                    response: Response<GoodsGetResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Retrofit", "success GET goods list...")
                        val responseBody = response.body()
                        if(responseBody != null){
                            setListView(responseBody.data)
                        }
                    }else{
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<GoodsGetResponse>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setListView(data: List<Goods>) {
        goodsNames = mutableListOf()
        for(item in data){
            goodsNames.add(item.name)
        }
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, goodsNames)
        binding.lvEmergencyGoods.adapter = adapter
    }

    private fun showInputDialog() {
        val binding = DialogInputFormBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("재난 상황 발생 시 필요한 구호물자를 등록해주세요.")
            .setView(binding.root)
            .setPositiveButton("저장") { dialogInterface, i ->
                val inputText = binding.etGoods.text.toString()
                if (inputText.isNotEmpty()) {
                    goodsNames.add(inputText)
                    binding.etGoods.text.clear()
                    adapter.notifyDataSetChanged()

                    val requestBody = GoodsRequest(inputText)
                    createUserGoods(requestBody)
                }
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun createUserGoods(requestBody: GoodsRequest) {
        RetrofitObject.networkService.createReliefGoods(userIdToken, requestBody)
            .enqueue(object : Callback<GoodsPostResponse> {
                override fun onResponse(
                    call: Call<GoodsPostResponse>,
                    response: Response<GoodsPostResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if(responseBody != null){
                            Log.d("Retrofit", responseBody.data.name)
                        }
                    }else{
                         Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<GoodsPostResponse>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun showModifyDialog(pos: Int) {
        val binding = DialogInputFormBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("구호물자 수정")
            .setView(binding.root)
            .setPositiveButton("저장") { dialogInterface, i ->
                val inputText = binding.etGoods.text.toString()
                if (inputText.isNotEmpty()) {
                    goodsNames[pos] = inputText
                    binding.etGoods.text.clear()
                    adapter.notifyDataSetChanged()

                    val requestBody = GoodsRequest(inputText)
                    updateUserGoods(pos, requestBody)
                }
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateUserGoods(pos: Int, requestBody: GoodsRequest) {
        RetrofitObject.networkService.updateReliefGoods(userIdToken, pos, requestBody)
            .enqueue(object : Callback<GoodsPutResponse> {
                override fun onResponse(
                    call: Call<GoodsPutResponse>,
                    response: Response<GoodsPutResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if(responseBody != null){
                            Log.d("Retrofit", responseBody.data.name)
                        }
                    }else{
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<GoodsPutResponse>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun showDeleteDialog(pos: Int): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("구호물자를 삭제하시겠습니까?")
            .setPositiveButton("확인") { dialog, which ->
                goodsNames.removeAt(pos)
                adapter.notifyDataSetChanged()

                deleteUserGoods(pos)
            }
            .setNegativeButton("취소", null)
            .create()
            .show()

        return true
    }

    private fun deleteUserGoods(pos: Int) {
        RetrofitObject.networkService.deleteReliefGoods(userIdToken, pos)
            .enqueue(object : Callback<GoodsDeleteResponse> {
                override fun onResponse(
                    call: Call<GoodsDeleteResponse>,
                    response: Response<GoodsDeleteResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if(responseBody != null){
                            Log.d("Retrofit", responseBody.data)
                        }
                    }else{
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<GoodsDeleteResponse>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }
}