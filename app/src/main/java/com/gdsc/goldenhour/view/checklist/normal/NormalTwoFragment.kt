package com.gdsc.goldenhour.view.checklist.normal

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.gdsc.goldenhour.binding.BindingFragment
import com.gdsc.goldenhour.databinding.DialogInputFormBinding
import com.gdsc.goldenhour.databinding.FragmentNormalTwoBinding

// ArrayAdapter
class NormalTwoFragment :
    BindingFragment<FragmentNormalTwoBinding>(FragmentNormalTwoBinding::inflate) {
    private lateinit var goods: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listView: ListView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goods = mutableListOf("물", "방석")
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, goods)
        listView = binding.lvEmergencyGoods

        listView.adapter = adapter

        binding.fabEmergencyGoods.setOnClickListener {
            showInputDialog()
        }

        listView.setOnItemClickListener { adapterView, view, pos, l ->
            showModifyDialog(pos)
        }

        listView.setOnItemLongClickListener { adapterView, view, pos, l ->
            showDeleteDialog(pos)
        }
    }

    private fun showDeleteDialog(pos: Int): Boolean {
        AlertDialog.Builder(requireContext())
            .setTitle("구호물자를 삭제하시겠습니까?")
            .setPositiveButton("확인") { dialog, which ->
                goods.removeAt(pos)
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("취소", null)
            .create()
            .show()
        return true
    }

    private fun showModifyDialog(pos: Int) {
        val binding = DialogInputFormBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("구호물자 수정")
            .setView(binding.root)
            .setPositiveButton("저장") { dialogInterface, i ->
                val inputText = binding.etGoods.text.toString()
                if (inputText.isNotEmpty()) {
                    goods[pos] = inputText
                    binding.etGoods.text.clear()
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showInputDialog() {
        val binding = DialogInputFormBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("재난 상황 발생 시 필요한 구호물자를 등록해주세요.")
            .setView(binding.root)
            .setPositiveButton("저장") { dialogInterface, i ->
                val inputText = binding.etGoods.text.toString()
                if (inputText.isNotEmpty()) {
                    goods.add(inputText)
                    binding.etGoods.text.clear()
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }
}