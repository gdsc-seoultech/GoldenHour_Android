package com.gdsc.goldenhour.view.call

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.ActivitySmsButtonBinding
import com.gdsc.goldenhour.network.RetrofitObject
import com.gdsc.goldenhour.network.model.GuideList
import com.gdsc.goldenhour.network.model.SituationList
import com.gdsc.goldenhour.network.model.TypeSituationList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.URL

class smsButtonActivity : AppCompatActivity() {
    lateinit var binding: ActivitySmsButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsButtonBinding.inflate(layoutInflater)

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