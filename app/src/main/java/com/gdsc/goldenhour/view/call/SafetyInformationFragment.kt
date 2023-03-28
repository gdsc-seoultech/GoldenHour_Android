package com.gdsc.goldenhour.view.call

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.databinding.FragmentSafetyInformationBinding

class SafetyInformationFragment : Fragment() {

    lateinit var binding: FragmentSafetyInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSafetyInformationBinding.inflate(inflater, container, false)
        loadSafetyInformation()
        return binding.root
    }


    private fun loadSafetyInformation() {

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        val gridLayout = GridLayout(context)
        gridLayout.columnCount = 2

        val array: Array<String> = arrayOf("안전 지역에 있다", "사고 현장에 있다", "사고로 부상을 당했다", "사고로 움직이지 못한다")

        for(i in 0 until 4) {
            val button= Button(context)
            button.text = array[i]
            button.setOnClickListener {
                Toast.makeText(context, "${array[i]}", Toast.LENGTH_SHORT).show()

                val numFragment = NumFragment()
                val totalBundle = Bundle()
                val tmp: String = arguments?.getString("key") + "\n" + "신고자 안전 정보 : " + array[i]
                totalBundle.putString("key", tmp)
                totalBundle.putString("type", requireArguments().getString("type"))
                numFragment.arguments = totalBundle

                transaction.replace(R.id.container, numFragment).commit()
            }
            gridLayout.addView(button)

        }
        binding.layout.addView(gridLayout)
    }
}