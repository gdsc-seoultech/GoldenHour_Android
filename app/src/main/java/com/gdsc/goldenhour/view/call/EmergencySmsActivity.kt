package com.gdsc.goldenhour.view.call

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gdsc.goldenhour.databinding.ActivityEmergencySmsBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.SituationList
import com.gdsc.goldenhour.network.model.TypeSituationList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmergencySmsActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmergencySmsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencySmsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        loadSituationList("119")
    }

    private fun loadSituationList(type: String) {
        RetrofitObject.networkService.getSituationList(type)
            .enqueue(object : Callback<SituationList> {
                override fun onResponse(call: Call<SituationList>, response: Response<SituationList>) {


                    Log.d("Retrofit", "success guide fragment")
                    val body = response.body()
                    if(!body.toString().isEmpty()) {
                        binding.txt.text = body.toString()
                    } else {
                        Log.d("tag", "empty")
                    }
                    Log.d("tag", body.toString())

                }

                override fun onFailure(call: Call<SituationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }

    private fun loadTypeSituation(id: Int) {

        RetrofitObject.networkService.getTypeSituationList(id)
            .enqueue(object : Callback<TypeSituationList> {
                override fun onResponse(call: Call<TypeSituationList>, response: Response<TypeSituationList>) {

                    Log.d("tag", "success guide fragment")
                    val body = response.body()
                    if(!body.toString().isEmpty()) {
                        binding.txt.text = body.toString()
                    } else {
                        Log.d("tag", "empty")
                    }
                    Log.d("tag", body.toString())

                }

                override fun onFailure(call: Call<TypeSituationList>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }
}