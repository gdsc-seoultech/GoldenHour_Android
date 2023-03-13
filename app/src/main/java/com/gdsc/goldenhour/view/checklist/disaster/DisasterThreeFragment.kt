package com.gdsc.goldenhour.view.checklist.disaster

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentDisasterThreeBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.Contact
import com.gdsc.goldenhour.network.model.ContactList
import com.gdsc.goldenhour.view.checklist.disaster.adapter.ContactAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisasterThreeFragment :
    BindingFragment<FragmentDisasterThreeBinding>(FragmentDisasterThreeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gsa = GoogleSignIn.getLastSignedInAccount(requireContext())
        loadUserContactList(gsa?.idToken.toString())
    }

    private fun loadUserContactList(idToken: String) {
        RetrofitObject.networkService.getContactList(idToken)
            .enqueue(object : Callback<ContactList> {
                override fun onResponse(
                    call: Call<ContactList>,
                    response: Response<ContactList>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Retrofit", "success contact fragment")

                        val body = response.body()
                        body?.let {
                            setRecyclerView(it.data)
                        }
                    }
                }

                override fun onFailure(call: Call<ContactList>, t: Throwable) {
                    Log.d("Retrofit", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun setRecyclerView(data: List<Contact>) {
        val recyclerView = binding.contactRecyclerView
        recyclerView.adapter = ContactAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(decoration)
    }
}