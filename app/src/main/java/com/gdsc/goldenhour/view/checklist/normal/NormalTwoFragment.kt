package com.gdsc.goldenhour.view.checklist.normal

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.DialogInputFormBinding
import com.gdsc.goldenhour.databinding.FragmentNormalTwoBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.*
import com.gdsc.goldenhour.view.checklist.normal.adapter.GoodsAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *  조회 -> 요청: token            응답: id, name
 *  추가 -> 요청: token, name      응답: id, name
 *  수정 -> 요청: token, id, name  응답: name
 *  삭제 -> 요청: token, id        응답: message
 */

class NormalTwoFragment :
    BindingFragment<FragmentNormalTwoBinding>(FragmentNormalTwoBinding::inflate) {
    private lateinit var userIdToken: String
    private lateinit var goodsList: MutableList<Goods>
    private lateinit var goodsAdapter: GoodsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserIdToken()
        getUserGoods()

        binding.fabReliefGoods.setOnClickListener {
            showInputDialog()
        }
    }

    private fun initUserIdToken() {
        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())
        userIdToken = gsa?.idToken.toString()
    }

    private fun getUserGoods() {
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

    private fun setRecyclerView(data: List<Goods>) {
        // 다른 곳에서 항목을 수정, 삭제할 수 있도록 Mutable로 변경
        goodsList = data.toMutableList()

        goodsAdapter = GoodsAdapter(data)

        // 아이템을 클릭하면 내용을 수정할 수 있도록
        goodsAdapter.setMyItemClickListener(object : GoodsAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                showModifyDialog(pos)
            }
        })

        // 아이템을 길게 누르면 삭제할 수 있도록
        goodsAdapter.setMyItemLongClickListener(object : GoodsAdapter.OnItemLongClickListener {
            override fun onItemLongClick(pos: Int) {
                showDeleteDialog(pos)
            }
        })

        val recyclerView = binding.rvEmergencyGoods
        recyclerView.adapter = goodsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
    }

    private fun showInputDialog() {
        val binding = DialogInputFormBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("재난 상황 발생 시 필요한 구호물자를 등록해주세요.")
            .setView(binding.root)
            .setPositiveButton("저장") { dialogInterface, i ->
                val inputText = binding.etGoods.text.toString()

                if (inputText.isNotEmpty()) {
                    // todo: 뷰에서는 제일 마지막에 항목을 추가한다.
                    val insertedPosition = goodsList.size
                    goodsList.add(insertedPosition, Goods(0, inputText))
                    goodsAdapter.notifyItemInserted(insertedPosition)

                    binding.etGoods.text.clear()

                    // todo: 서버에 새 항목을 등록한다.
                    createUserGoods(GoodsRequest(inputText))
                }
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun createUserGoods(requestBody: GoodsRequest) {
        RetrofitObject.networkService.createReliefGoods(userIdToken, requestBody)
            .enqueue(object : Callback<GoodsCreateResponse> {
                override fun onResponse(
                    call: Call<GoodsCreateResponse>,
                    response: Response<GoodsCreateResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val insertedItem = responseBody.data
                            goodsList.last().id = insertedItem.id
                            Log.d("Retrofit", "${insertedItem.id} success POST goods item!!!")
                        }
                    } else {
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<GoodsCreateResponse>, t: Throwable) {
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
                    val item = goodsList[pos]
                    item.name = inputText
                    goodsAdapter.notifyItemChanged(pos)

                    // 서버에서는 항목의 id를 기준으로 내용 변경
                    updateUserGoods(item.id, GoodsRequest(item.name))
                }
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateUserGoods(itemId: Int, requestBody: GoodsRequest) {
        RetrofitObject.networkService.updateReliefGoods(userIdToken, itemId, requestBody)
            .enqueue(object : Callback<GoodsUpdateResponse> {
                override fun onResponse(
                    call: Call<GoodsUpdateResponse>,
                    response: Response<GoodsUpdateResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.d("Retrofit", responseBody.data.name)
                        }
                    } else {
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<GoodsUpdateResponse>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun showDeleteDialog(pos: Int): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("구호물자를 삭제하시겠습니까?")
            .setPositiveButton("확인") { dialog, which ->
                val id = goodsList[pos].id

                // 뷰에서는 클릭한 위치에 따라 삭제한다.
                goodsList.removeAt(pos)
                goodsAdapter.notifyItemRemoved(pos)

                // 서버에서는 항목의 id를 기준으로 삭제한다.
                deleteUserGoods(id)
            }
            .setNegativeButton("취소", null)
            .create()
            .show()
        return true
    }

    private fun deleteUserGoods(itemId: Int) {
        RetrofitObject.networkService.deleteReliefGoods(userIdToken, itemId)
            .enqueue(object : Callback<GoodsDeleteResponse> {
                override fun onResponse(
                    call: Call<GoodsDeleteResponse>,
                    response: Response<GoodsDeleteResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.d("Retrofit", responseBody.data)
                        }
                    } else {
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