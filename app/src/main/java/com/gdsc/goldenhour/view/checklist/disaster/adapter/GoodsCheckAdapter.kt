package com.gdsc.goldenhour.view.checklist.disaster.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.databinding.DisasterGoodsItemBinding
import com.gdsc.goldenhour.view.checklist.disaster.adapter.model.GoodsCheck


class GoodsCheckAdapter(
    private val context: Context,
    private val data: List<GoodsCheck>
): RecyclerView.Adapter<GoodsCheckAdapter.GoodsCheckViewHolder>() {

    inner class GoodsCheckViewHolder(
        private val binding: DisasterGoodsItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: GoodsCheck) {
            binding.tvGoodsName.text = item.name
            binding.cbGoods.isChecked = item.checked
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsCheckViewHolder {
        val view = DisasterGoodsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return GoodsCheckViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoodsCheckViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}