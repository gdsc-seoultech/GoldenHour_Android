package com.gdsc.goldenhour.view.checklist.normal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.databinding.NormalGoodsItemBinding
import com.gdsc.goldenhour.network.model.Goods

class GoodsAdapter(
    private val goods: List<Goods>
): RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>() {
    class GoodsViewHolder(
        private val binding: NormalGoodsItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Goods){
            binding.tvGoodsId.text = item.id.toString()
            binding.tvGoodsName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val view = NormalGoodsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GoodsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        holder.bind(goods[position])
    }

    override fun getItemCount(): Int = goods.size
}