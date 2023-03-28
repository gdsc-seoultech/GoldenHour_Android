package com.gdsc.goldenhour.view.call

import android.content.Context
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
import com.gdsc.goldenhour.databinding.FragmentNumBinding
import com.gdsc.goldenhour.databinding.FragmentSafetyInformationBinding

class NumFragment : Fragment() {

    lateinit var binding: FragmentNumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNumBinding.inflate(inflater, container, false)
        loadNum()
        return binding.root
    }

    private fun loadNum() {

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        val gridLayout = GridLayout(context)
        gridLayout.columnCount = 4

        val array: Array<String> = arrayOf("알 수 없다", "1명", "2명", "3명 이상")

        for(i in 0 until 4) {
            val button= Button(context)
            button.text = array[i]
            button.setOnClickListener {
                Toast.makeText(context, "${array[i]}", Toast.LENGTH_SHORT).show()

                val peopleInfoFragment = PeopleInfoFragment()
                val totalBundle = Bundle()
                val tmp: String = arguments?.getString("key") + "\n" + "인원 수 : " + array[i]
                totalBundle.putString("key", tmp)
                totalBundle.putString("type", requireArguments().getString("type"))
                peopleInfoFragment.arguments = totalBundle

                Log.d("tag", "detail " + tmp)
                transaction.replace(R.id.container, peopleInfoFragment).commit()

            }
            gridLayout.addView(button)

        }
        binding.layout.addView(gridLayout)
    }
}