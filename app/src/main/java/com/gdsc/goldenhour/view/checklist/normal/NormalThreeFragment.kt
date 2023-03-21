package com.gdsc.goldenhour.view.checklist.normal

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.ContactInputFormBinding
import com.gdsc.goldenhour.databinding.FragmentNormalThreeBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.Contact
import com.gdsc.goldenhour.network.model.ContactCreateResponse
import com.gdsc.goldenhour.network.model.ContactReadResponse
import com.gdsc.goldenhour.network.model.ContactRequest
import com.gdsc.goldenhour.view.checklist.normal.adapter.NormalContactAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NormalThreeFragment :
    BindingFragment<FragmentNormalThreeBinding>(FragmentNormalThreeBinding::inflate) {
    private lateinit var userIdToken: String
    private lateinit var contactAdapter: NormalContactAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserIdToken()

        getEmergencyContacts()

        binding.fabEmergencyContact.setOnClickListener {
            showInputDialog()
        }
    }

    private fun initUserIdToken() {
        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())
        userIdToken = gsa?.idToken.toString()
    }

    private fun getEmergencyContacts() {
        RetrofitObject.networkService.readEmergencyContacts(userIdToken)
            .enqueue(object : Callback<ContactReadResponse> {
                override fun onResponse(
                    call: Call<ContactReadResponse>,
                    response: Response<ContactReadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            setRecyclerView(responseBody.data.toMutableList())
                        }

                        Log.d("Retrofit", "success GET contact list...")
                    } else {
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ContactReadResponse>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }

            })
    }

    private fun setRecyclerView(data: MutableList<Contact>) {
        contactAdapter = NormalContactAdapter(data)
        val recyclerView = binding.rvEmergencyContact
        recyclerView.adapter = contactAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
    }

    private fun showInputDialog() {
        val binding = ContactInputFormBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("재난상황 발생 시 필요한 비상연락망을 등록해주세요.")
            .setView(binding.root)
            .setPositiveButton("저장") { dialogInterface, i ->
                // 서버에 새 항목 등록하고 UI 갱신
                val name = binding.etName.text.toString()
                val phoneNumber = binding.etPhoneNumber.text.toString()
                createUserContact(ContactRequest(name, phoneNumber))
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun createUserContact(contactRequest: ContactRequest) {
        RetrofitObject.networkService.createEmergencyContact(userIdToken, contactRequest)
            .enqueue(object : Callback<ContactCreateResponse> {
                override fun onResponse(
                    call: Call<ContactCreateResponse>,
                    response: Response<ContactCreateResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val item = responseBody.data
                            addItemInAdapter(item)
                            Log.d("Retrofit", "${item.id} success POST contact item!!!")
                        }
                    } else {
                        Log.e("Retrofit", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ContactCreateResponse>, t: Throwable){
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun addItemInAdapter(item: Contact) {
        contactAdapter.apply {
            addItem(item)
            notifyItemInserted(itemCount)
        }
    }
}