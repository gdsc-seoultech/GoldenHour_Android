package com.gdsc.goldenhour.view.checklist.normal

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.FragmentNormalThreeBinding
import com.gdsc.goldenhour.view.checklist.normal.data.DataSource

class NormalThreeFragment :
    BindingFragment<FragmentNormalThreeBinding>(FragmentNormalThreeBinding::inflate) {
    private lateinit var contacts: ArrayList<HashMap<String, String>>
    private lateinit var adapter: SimpleAdapter
    private lateinit var listView: ListView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contacts = DataSource().loadEmergencyContacts()
        adapter = SimpleAdapter(
            requireContext(),
            contacts,
            android.R.layout.simple_list_item_2,
            arrayOf("name", "phoneNumber"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        listView = binding.lvEmergencyContact
        listView.adapter = adapter

        binding.fabEmergencyContact.setOnClickListener {
//            showInputDialog()
        }

//        listView.setOnItemClickListener { adapterView, view, pos, l ->
////            showModifyDialog(pos)
//        }
//
//        listView.setOnItemLongClickListener { adapterView, view, pos, l ->
////            showDeleteDialog(pos)
//        }

    }

//    private fun showInputDialog() {
//        val binding = DialogInputFormBinding.inflate(layoutInflater)
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("재난 상황 발생 시 .")
//            .setView(binding.root)
//            .setPositiveButton("저장") { dialogInterface, i ->
//                val inputText = binding.etGoods.text.toString()
//                if (inputText.isNotEmpty()) {
//                    contacts.add()
//                    binding.etGoods.text.clear()
//                    adapter.notifyDataSetChanged()
//
//                }
//            }
//            .setNegativeButton("취소", null)
//        val dialog = builder.create()
//        dialog.show()
//    }
}