package com.gdsc.goldenhour.view.checklist.disaster

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentDisasterThreeBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.Contact
import com.gdsc.goldenhour.network.model.ContactReadResponse
import com.gdsc.goldenhour.view.checklist.disaster.adapter.DisasterContactAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisasterThreeFragment :
    BindingFragment<FragmentDisasterThreeBinding>(FragmentDisasterThreeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserContactData()
    }

    private fun loadUserContactData() {
        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())
        val userIdToken = gsa?.idToken.toString()

        RetrofitObject.networkService.readEmergencyContacts(userIdToken)
            .enqueue(object : Callback<ContactReadResponse> {
                override fun onResponse(
                    call: Call<ContactReadResponse>,
                    response: Response<ContactReadResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            setRecyclerView(responseBody.data)
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

    private fun setRecyclerView(data: List<Contact>) {
        val recyclerView = binding.rvContacts
        val adapter = DisasterContactAdapter(data)

        adapter.setMyItemClickListener(object: DisasterContactAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                Log.d("CLICK", "$position clicked...")
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
    }
}