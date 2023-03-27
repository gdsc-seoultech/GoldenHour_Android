package com.gdsc.goldenhour.view.call

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.gdsc.goldenhour.databinding.FragmentPeopleInfoBinding

class PeopleInfoFragment : Fragment() {

    lateinit var binding: FragmentPeopleInfoBinding
    lateinit var tmp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPeopleInfoBinding.inflate(inflater, container, false)
        loadPeopleInfo()

        val type = requireArguments().getString("type")
        Log.d("tag", type.toString())
        binding.resBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+type))
            intent.putExtra("sms_body", tmp)
            startActivity(intent)
        }
        return binding.root
    }

    private fun loadPeopleInfo() {

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        val gridLayout = GridLayout(context)
        gridLayout.columnCount = 4

        val array: Array<String> = arrayOf("어린이", "임산부", "장애인", "노인", "청소년", "외국인", "남자", "여자")

        for(i in 0 until 8) {
            val button= Button(context)
            button.text = array[i]
            button.setOnClickListener {
                Toast.makeText(context, "${array[i]}", Toast.LENGTH_SHORT).show()

                tmp = arguments?.getString("key") + "\n" + "인원 정보 : " + array[i]
                Log.d("tag", "result : " + tmp)
                binding.resBtn.visibility = View.VISIBLE
            }
            gridLayout.addView(button)

        }
        binding.layout.addView(gridLayout)
    }
}